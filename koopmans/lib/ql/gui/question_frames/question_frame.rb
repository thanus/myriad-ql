module QL
  module GUI
    class QuestionFrame
      include Callback
      attr_reader :enabled, :condition, :widget

      def initialize(ast_question, condition=nil)
        @ast_question = ast_question
        @condition = condition
        @enabled = true
        @tk_frame = TkFrame.new.grid
        Label.new(@tk_frame, label)
        create_corresponding_widget
      end

      def create_corresponding_widget
        @widget = widget_type.new(@tk_frame)
      end

      def render
        store_default_value
        listen_to_widget
      end

      def store_default_value
        @value = @widget.default_value
        value_changed
      end

      def listen_to_widget
        @widget.listen do |changed_value|
          p changed_value
          @value = changed_value
          value_changed
        end
      end

      def value_changed
        store_value
        callback
      end

      def store_value
        VariableTable.store(variable_name, literal_type.new(@value))
      end

      def reload
        if @condition
          check_condition
        end
      end

      def check_condition
        @condition.accept(Evaluator.new).to_value ? enable : disable
      end

      def enable
        @tk_frame.grid
        @enabled = true
      end

      def disable
        @tk_frame.grid_remove
        @enabled = false
      end

      def to_json
        { label => @value }
      end

      def variable_name
        @ast_question.variable.name
      end

      def label
        @ast_question.label.to_value
      end

      def literal_type
        @ast_question.type.literal_type
      end

      def widget_type
        @ast_question.type.widget
      end

      def default_value
        @ast_question.type.default_value
      end
    end
  end
end