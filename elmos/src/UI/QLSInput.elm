module UI.QLSInput exposing (Model, Msg, init, asStylesheet, setForm, update, view)

import Html exposing (Html, textarea, div, pre, text)
import Html.Attributes exposing (class, defaultValue, rows, cols, class, style)
import Html.Events exposing (onInput)
import QLS.AST exposing (StyleSheet)
import QL.AST exposing (Form)
import QLS.Parser as Parser
import QLS.TypeChecker


type Model
    = Model String (Maybe StyleSheet) (Maybe Form)


type Msg
    = OnInput String


init : Maybe Form -> Model
init form =
    Model "" Nothing form
        |> update (OnInput exampleDsl)


setForm : Maybe Form -> Model -> Model
setForm form (Model input styleSheet _) =
    Model input styleSheet form


exampleDsl : String
exampleDsl =
    """stylesheet taxOfficeExample
  page Housing {
    section "Buying"
      question hasBoughtHouse2
        widget checkbox
    section "Loaning"
      question hasMaintLoan
  }

  page Selling {
    section "Selling" {
      question hasSoldHouse
        widget radio("Yes", "No")
      section "You sold a house" {
        question sellingPrice
          widget spinbox
        question privateDebt
          widget spinbox
        question valueResidue
        default money {
          width: 400
          font: "Arial"
          fontsize: 14
          color: #999999
          widget spinbox
        }
      }
    }
    default boolean widget radio("Yes", "No")
  }
"""


asStylesheet : Model -> Maybe StyleSheet
asStylesheet (Model _ maybeStylesheet _) =
    maybeStylesheet


update : Msg -> Model -> Model
update msg (Model _ _ parsedForm) =
    case msg of
        OnInput newInput ->
            Model newInput (Parser.parse newInput) parsedForm


view : Model -> Html Msg
view (Model rawText parsedStyleSheet parsedForm) =
    div []
        [ div [ class "row" ]
            [ div [ class "col-md-6" ]
                [ Html.form [ class "form" ]
                    [ textarea
                        [ defaultValue rawText
                        , rows 20
                        , cols 45
                        , class "form-control"
                        , style [ ( "width", "100%" ), ( "resize", "none" ), ( "font-family", "courier" ) ]
                        , onInput OnInput
                        ]
                        []
                    ]
                ]
            , div [ class "col-md-6" ]
                [ text <| toString <| Maybe.withDefault [] <| Maybe.map2 QLS.TypeChecker.check parsedForm parsedStyleSheet
                ]
            ]
        , pre [] [ text <| toString parsedStyleSheet ]
        ]
