require 'active_support/inflector'

module Prophet
  module Ast
    class Node < Struct
      # We want to use Enumerable's implementation of select, ...
      prepend Enumerable

      def visit(visitor)
        visitor.send(method_name, self)
      end

      def each(&block)
        yield self
        children.each { |child| child.each(&block) }
      end

      # Selects all children that are of given type(s)
      #
      #   ast.select_by_type(:identifier)
      #   ast.select_by_type('question', 'question_with_value')
      def select_by_type(*types)
        qualified_types = types.map do |type|
          "Prophet::Ast::#{type.to_s.camelize}".constantize
        end
        select { |node| qualified_types.any? { |type| node.is_a? type } }
      end

      def children
        values.each_with_object([]) do |child, memo|
          if child.is_a? Node
            memo << child
          elsif child.is_a? Array
            child.each { |c| memo << c if c.is_a? Node }
          end
        end
      end

      # Finds the leftmost child that is a Parslet::Slice and returns its line
      # and column
      def line_and_column
        first_value = values.first
        case first_value
        when Parslet::Slice, Node
          first_value.line_and_column
        when Array
          first_value.first.line_and_column
        else
          []
        end
      end

      def node_name
        self.class.name.demodulize.underscore
      end

      def method_name
        "visit_#{node_name}"
      end
    end
  end
end
