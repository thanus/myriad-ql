module QLS
  module GUI
    module GUIStylesheet
      attr_writer :question_frame_styles

      def render
        apply_styles
        super
      end

      def apply_styles
        normal_question_frames.each do |question_frame|
          new_style = @question_frame_styles[question_frame.name]
          next if new_style.nil?
          apply_widget(question_frame, new_style.widget)
          apply_width(question_frame, new_style.width)
        end
      end

      def apply_widget(question_frame, widget)
        return unless widget
        question_frame.widget = widget
      end

      def apply_width(question_frame, width)
        return unless width
        question_frame.tk_frame.width = width
      end

      def normal_question_frames
        @question_frames.each.select do |question_frame|
          question_frame.class == QL::GUI::QuestionFrame
        end
      end
    end
  end
end