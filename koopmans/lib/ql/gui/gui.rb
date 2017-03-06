require 'tk'

module QL
  module GUI
    class GUI
      attr_accessor :questions

      def initialize(ql_ast, qls_ast, type_checker)
        return if check(type_checker) == 'quit'
        @questions = Hash.new
        FormBuilder.new(ql_ast, self)
        # StylesheetBuilder.new(qls_ast, ql_ast, self)

        SubmitButton.new(self)
        Tk.mainloop
      end

      def reload_questions
        @questions.each_value(&:reload)
      end

      def submit
        p @questions.each_value.select { |question| question.enabled }.map(&:to_json)
      end

      def check(type_checker)
        if !type_checker[:errors].empty?
          Tk.messageBox(
              type:    'ok',
              icon:    'error',
              title:   'Errors found!',
              message: type_checker[:errors].join('\n')
          )
          return 'quit'
        elsif !type_checker[:warnings].empty?
          Tk.messageBox(
              type:    'ok',
              icon:    'warning',
              title:   'Warnings found!',
              message: type_checker[:warnings].join('\n')
          )
          return 'continue'
        end
      end
    end
  end
end