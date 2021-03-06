from gui.traversals.default_widget_getter import DefaultWidgetGetter
from gui.traversals.gui_builder import GuiBuilder
from ql.traversals.question_finder import QuestionFinder
from qls.traversals.styling_widget_getter import StylingWidgetGetter


class QlsGuiBuilder(GuiBuilder):

    def __init__(self, app, listener, on_exit, widgets, form):
        super().__init__(app, listener, on_exit, widgets)
        self.form = form

    def build(self, node):
        self.visit(node, [])

    def visit_layout(self, node, stylings):
        self.app.startTabbedFrame(node.name)
        self.app.setTabbedFrameTabExpand(node.name, True)
        for element in node.body:
            self.visit(element, stylings)
        self.app.stopTabbedFrame()
        self.create_exit_button()

    def visit_styled_layout(self, node, stylings):
        self.visit_layout(node, stylings + node.stylings)

    def visit_page(self, node, stylings):
        self.app.startTab(node.name)
        for element in node.body:
            self.visit(element, stylings)
        self.app.stopTab()

    def visit_styled_page(self, node, stylings):
        self.visit_page(node, stylings + node.stylings)

    def visit_section(self, node, stylings):
        self.app.startLabelFrame(node.name)
        for element in node.body:
            self.visit(element, stylings)
        self.app.stopLabelFrame()

    def visit_styled_section(self, node, stylings):
        self.visit_section(node, stylings + node.stylings)

    def visit_question_anchor(self, node, stylings):
        question = QuestionFinder(node.name).find(self.form)
        self.visit(question, stylings)

    def visit_styled_question_anchor(self, node, stylings):
        self.visit_question_anchor(node, stylings + [node.styling])

    def visit_question(self, node, stylings):
        constructor = self.get_widget_constructor(node.datatype, stylings)

        widget = constructor(self.app, node)
        widget.set_listener(self.listener)

        for styling in stylings:
            styling.apply_on(widget)

        self.widgets[node.name] = widget

    def visit_computed_question(self, node, stylings):
        self.visit_question(node, stylings)
        self.widgets[node.name].disable()

    def get_widget_constructor(self, datatype, stylings):
        constructor = None

        styling_getter = StylingWidgetGetter(datatype)
        default_getter = DefaultWidgetGetter()

        for styling in stylings:
            if styling_getter.get(styling):
                constructor = styling_getter.get(styling)

        if constructor is None:
            constructor = default_getter.get(datatype)

        return constructor
