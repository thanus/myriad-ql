module Combine.Extra exposing (whitespace1, trimmed)

import Combine exposing (Parser, fail, succeed, whitespace, (>>=), (<*), (*>))


whitespace1 : Parser s String
whitespace1 =
    whitespace >>= nonEmpty


nonEmpty : String -> Parser s String
nonEmpty s =
    if String.isEmpty s then
        fail "Expected non empty parse"
    else
        succeed s


trimmed : Parser s p -> Parser s p
trimmed parser =
    whitespace *> parser <* whitespace
