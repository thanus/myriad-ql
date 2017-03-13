module QLS
  module AST
    class Widget
      include QL::AST

      def accept(visitor)
        visitor.visit_widget(self)
      end
    end

    class SliderWidget < Widget
      attr_reader :minimum, :maximum

      def initialize(minimum, maximum)
        @minimum = minimum
        @maximum = maximum
      end

      def is_compatible_with
        [IntegerType, MoneyType, DecimalType]
      end
    end

    class SpinboxWidget < Widget
      def is_compatible_with
        [IntegerType, MoneyType, DecimalType]
      end
    end

    class TextWidget < Widget
      def is_compatible_with
        [IntegerType, DateType, DecimalType, StringType, MoneyType]
      end
    end

    class RadioWidget < Widget
      attr_reader :true_text, :false_text

      def initialize(true_text, false_text)
        @true_text  = true_text
        @false_text = false_text
      end

      def is_compatible_with
        [BooleanType]
      end
    end

    class CheckboxWidget < Widget
      def is_compatible_with
        [BooleanType]
      end
    end

    class DropdownWidget < Widget
      attr_reader :true_text, :false_text

      def initialize(true_text, false_text)
        @true_text  = true_text
        @false_text = false_text
      end

      def is_compatible_with
        [BooleanType]
      end
    end
  end
end