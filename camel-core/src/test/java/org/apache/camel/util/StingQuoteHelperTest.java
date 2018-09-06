begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
package|;
end_package

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|StingQuoteHelperTest
specifier|public
class|class
name|StingQuoteHelperTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
DECL|method|testSplitSafeQuote ()
specifier|public
name|void
name|testSplitSafeQuote
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|StringQuoteHelper
operator|.
name|splitSafeQuote
argument_list|(
literal|null
argument_list|,
literal|','
argument_list|)
argument_list|)
expr_stmt|;
name|String
index|[]
name|out
init|=
name|StringQuoteHelper
operator|.
name|splitSafeQuote
argument_list|(
literal|""
argument_list|,
literal|','
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|out
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|out
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|out
operator|=
name|StringQuoteHelper
operator|.
name|splitSafeQuote
argument_list|(
literal|"   "
argument_list|,
literal|','
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|out
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|out
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|out
operator|=
name|StringQuoteHelper
operator|.
name|splitSafeQuote
argument_list|(
literal|"   "
argument_list|,
literal|','
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|out
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"   "
argument_list|,
name|out
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|out
operator|=
name|StringQuoteHelper
operator|.
name|splitSafeQuote
argument_list|(
literal|"Camel"
argument_list|,
literal|','
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|out
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Camel"
argument_list|,
name|out
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|out
operator|=
name|StringQuoteHelper
operator|.
name|splitSafeQuote
argument_list|(
literal|"Hello Camel"
argument_list|,
literal|','
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|out
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello Camel"
argument_list|,
name|out
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|out
operator|=
name|StringQuoteHelper
operator|.
name|splitSafeQuote
argument_list|(
literal|"Hello,Camel"
argument_list|,
literal|','
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|out
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello"
argument_list|,
name|out
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Camel"
argument_list|,
name|out
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|out
operator|=
name|StringQuoteHelper
operator|.
name|splitSafeQuote
argument_list|(
literal|"Hello,Camel,Bye,World"
argument_list|,
literal|','
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|out
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello"
argument_list|,
name|out
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Camel"
argument_list|,
name|out
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Bye"
argument_list|,
name|out
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"World"
argument_list|,
name|out
index|[
literal|3
index|]
argument_list|)
expr_stmt|;
name|out
operator|=
name|StringQuoteHelper
operator|.
name|splitSafeQuote
argument_list|(
literal|"'Hello,Camel','Bye,World'"
argument_list|,
literal|','
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|out
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello,Camel"
argument_list|,
name|out
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Bye,World"
argument_list|,
name|out
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|out
operator|=
name|StringQuoteHelper
operator|.
name|splitSafeQuote
argument_list|(
literal|"'Hello,Camel',\"Bye,World\""
argument_list|,
literal|','
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|out
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello,Camel"
argument_list|,
name|out
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Bye,World"
argument_list|,
name|out
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|out
operator|=
name|StringQuoteHelper
operator|.
name|splitSafeQuote
argument_list|(
literal|"\"Hello,Camel\",'Bye,World'"
argument_list|,
literal|','
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|out
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello,Camel"
argument_list|,
name|out
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Bye,World"
argument_list|,
name|out
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|out
operator|=
name|StringQuoteHelper
operator|.
name|splitSafeQuote
argument_list|(
literal|"\"Hello,Camel\",\"Bye,World\""
argument_list|,
literal|','
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|out
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello,Camel"
argument_list|,
name|out
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Bye,World"
argument_list|,
name|out
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|out
operator|=
name|StringQuoteHelper
operator|.
name|splitSafeQuote
argument_list|(
literal|"'Hello Camel', 'Bye World'"
argument_list|,
literal|','
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|out
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello Camel"
argument_list|,
name|out
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Bye World"
argument_list|,
name|out
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|out
operator|=
name|StringQuoteHelper
operator|.
name|splitSafeQuote
argument_list|(
literal|"'Hello Camel', ' Bye World'"
argument_list|,
literal|','
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|out
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello Camel"
argument_list|,
name|out
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|" Bye World"
argument_list|,
name|out
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|out
operator|=
name|StringQuoteHelper
operator|.
name|splitSafeQuote
argument_list|(
literal|"'http:', ' '"
argument_list|,
literal|','
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|out
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http:"
argument_list|,
name|out
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|" "
argument_list|,
name|out
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|out
operator|=
name|StringQuoteHelper
operator|.
name|splitSafeQuote
argument_list|(
literal|"'http:', ''"
argument_list|,
literal|','
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|out
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http:"
argument_list|,
name|out
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|out
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|out
operator|=
name|StringQuoteHelper
operator|.
name|splitSafeQuote
argument_list|(
literal|"'Hello Camel', 5, true"
argument_list|,
literal|','
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|out
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello Camel"
argument_list|,
name|out
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"5"
argument_list|,
name|out
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"true"
argument_list|,
name|out
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
name|out
operator|=
name|StringQuoteHelper
operator|.
name|splitSafeQuote
argument_list|(
literal|"'Hello Camel',5,true"
argument_list|,
literal|','
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|out
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello Camel"
argument_list|,
name|out
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"5"
argument_list|,
name|out
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"true"
argument_list|,
name|out
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
name|out
operator|=
name|StringQuoteHelper
operator|.
name|splitSafeQuote
argument_list|(
literal|"   'Hello Camel',  5   ,  true   "
argument_list|,
literal|','
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|out
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello Camel"
argument_list|,
name|out
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"5"
argument_list|,
name|out
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"true"
argument_list|,
name|out
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
name|out
operator|=
name|StringQuoteHelper
operator|.
name|splitSafeQuote
argument_list|(
literal|"*, '', 'arg3'"
argument_list|,
literal|','
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|out
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"*"
argument_list|,
name|out
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|out
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"arg3"
argument_list|,
name|out
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testLastIsQuote ()
specifier|public
name|void
name|testLastIsQuote
parameter_list|()
throws|throws
name|Exception
block|{
name|String
index|[]
name|out
init|=
name|StringQuoteHelper
operator|.
name|splitSafeQuote
argument_list|(
literal|" ${body}, 5, 'Hello World'"
argument_list|,
literal|','
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|out
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"${body}"
argument_list|,
name|out
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"5"
argument_list|,
name|out
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|out
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
name|out
operator|=
name|StringQuoteHelper
operator|.
name|splitSafeQuote
argument_list|(
literal|" ${body}, 5, \"Hello World\""
argument_list|,
literal|','
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|out
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"${body}"
argument_list|,
name|out
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"5"
argument_list|,
name|out
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|out
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSingleInDoubleQuote ()
specifier|public
name|void
name|testSingleInDoubleQuote
parameter_list|()
throws|throws
name|Exception
block|{
name|String
index|[]
name|out
init|=
name|StringQuoteHelper
operator|.
name|splitSafeQuote
argument_list|(
literal|"\"Hello O'Connor\", 5, 'foo bar'"
argument_list|,
literal|','
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|out
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello O'Connor"
argument_list|,
name|out
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"5"
argument_list|,
name|out
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo bar"
argument_list|,
name|out
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
name|out
operator|=
name|StringQuoteHelper
operator|.
name|splitSafeQuote
argument_list|(
literal|"\"Hello O'Connor O'Bannon\", 5, 'foo bar'"
argument_list|,
literal|','
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|out
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello O'Connor O'Bannon"
argument_list|,
name|out
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"5"
argument_list|,
name|out
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo bar"
argument_list|,
name|out
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDoubleInSingleQuote ()
specifier|public
name|void
name|testDoubleInSingleQuote
parameter_list|()
throws|throws
name|Exception
block|{
name|String
index|[]
name|out
init|=
name|StringQuoteHelper
operator|.
name|splitSafeQuote
argument_list|(
literal|"'Hello O\"Connor', 5, 'foo bar'"
argument_list|,
literal|','
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|out
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello O\"Connor"
argument_list|,
name|out
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"5"
argument_list|,
name|out
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo bar"
argument_list|,
name|out
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
name|out
operator|=
name|StringQuoteHelper
operator|.
name|splitSafeQuote
argument_list|(
literal|"'Hello O\"Connor O\"Bannon', 5, 'foo bar'"
argument_list|,
literal|','
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|out
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello O\"Connor O\"Bannon"
argument_list|,
name|out
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"5"
argument_list|,
name|out
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo bar"
argument_list|,
name|out
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

