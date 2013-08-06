begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.language
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|ExchangeTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Expression
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
operator|.
name|tokenizer
operator|.
name|TokenizeLanguage
import|;
end_import

begin_class
DECL|class|TokenizerTest
specifier|public
class|class
name|TokenizerTest
extends|extends
name|ExchangeTestSupport
block|{
annotation|@
name|Override
DECL|method|populateExchange (Exchange exchange)
specifier|protected
name|void
name|populateExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|super
operator|.
name|populateExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"names"
argument_list|,
literal|"Claus,James,Willem"
argument_list|)
expr_stmt|;
block|}
DECL|method|testTokenizeHeader ()
specifier|public
name|void
name|testTokenizeHeader
parameter_list|()
throws|throws
name|Exception
block|{
name|Expression
name|exp
init|=
name|TokenizeLanguage
operator|.
name|tokenize
argument_list|(
literal|"names"
argument_list|,
literal|","
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|names
init|=
name|exp
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|names
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Claus"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"James"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Willem"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testTokenizeBody ()
specifier|public
name|void
name|testTokenizeBody
parameter_list|()
throws|throws
name|Exception
block|{
name|Expression
name|exp
init|=
name|TokenizeLanguage
operator|.
name|tokenize
argument_list|(
literal|","
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Hadrian,Charles"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|names
init|=
name|exp
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|names
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hadrian"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Charles"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testTokenizeBodyRegEx ()
specifier|public
name|void
name|testTokenizeBodyRegEx
parameter_list|()
throws|throws
name|Exception
block|{
name|Expression
name|exp
init|=
name|TokenizeLanguage
operator|.
name|tokenize
argument_list|(
literal|"(\\W+)\\s*"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"The little fox"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|names
init|=
name|exp
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|names
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"little"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"fox"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testTokenizeHeaderRegEx ()
specifier|public
name|void
name|testTokenizeHeaderRegEx
parameter_list|()
throws|throws
name|Exception
block|{
name|Expression
name|exp
init|=
name|TokenizeLanguage
operator|.
name|tokenize
argument_list|(
literal|"quote"
argument_list|,
literal|"(\\W+)\\s*"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"quote"
argument_list|,
literal|"Camel rocks"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|names
init|=
name|exp
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|names
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Camel"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"rocks"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testTokenizeManualConfiguration ()
specifier|public
name|void
name|testTokenizeManualConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
name|TokenizeLanguage
name|lan
init|=
operator|new
name|TokenizeLanguage
argument_list|()
decl_stmt|;
name|lan
operator|.
name|setHeaderName
argument_list|(
literal|"names"
argument_list|)
expr_stmt|;
name|lan
operator|.
name|setRegex
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|lan
operator|.
name|setToken
argument_list|(
literal|","
argument_list|)
expr_stmt|;
name|Expression
name|exp
init|=
name|lan
operator|.
name|createExpression
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|names
init|=
name|exp
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|names
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Claus"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"James"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Willem"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"names"
argument_list|,
name|lan
operator|.
name|getHeaderName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|","
argument_list|,
name|lan
operator|.
name|getToken
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|lan
operator|.
name|isRegex
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|lan
operator|.
name|isSingleton
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testTokenizePairSpecial ()
specifier|public
name|void
name|testTokenizePairSpecial
parameter_list|()
throws|throws
name|Exception
block|{
name|Expression
name|exp
init|=
name|TokenizeLanguage
operator|.
name|tokenizePair
argument_list|(
literal|"!"
argument_list|,
literal|"@"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"2011-11-11\n!James@!Claus@\n2 records"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|names
init|=
name|exp
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|names
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"James"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Claus"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testTokenizePair ()
specifier|public
name|void
name|testTokenizePair
parameter_list|()
throws|throws
name|Exception
block|{
name|Expression
name|exp
init|=
name|TokenizeLanguage
operator|.
name|tokenizePair
argument_list|(
literal|"[START]"
argument_list|,
literal|"[END]"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"2011-11-11\n[START]James[END]\n[START]Claus[END]\n2 records"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|names
init|=
name|exp
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|names
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"James"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Claus"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testTokenizePairIncludeTokens ()
specifier|public
name|void
name|testTokenizePairIncludeTokens
parameter_list|()
throws|throws
name|Exception
block|{
name|Expression
name|exp
init|=
name|TokenizeLanguage
operator|.
name|tokenizePair
argument_list|(
literal|"[START]"
argument_list|,
literal|"[END]"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"2011-11-11\n[START]James[END]\n[START]Claus[END]\n2 records"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|names
init|=
name|exp
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|names
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[START]James[END]"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"[START]Claus[END]"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testTokenizeXMLPair ()
specifier|public
name|void
name|testTokenizeXMLPair
parameter_list|()
throws|throws
name|Exception
block|{
name|Expression
name|exp
init|=
name|TokenizeLanguage
operator|.
name|tokenizeXML
argument_list|(
literal|"<person>"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"<persons><person>James</person><person>Claus</person><person>Jonathan</person><person>Hadrian</person></persons>"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|names
init|=
name|exp
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|names
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<person>James</person>"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<person>Claus</person>"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<person>Jonathan</person>"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<person>Hadrian</person>"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testTokenizeXMLPairNoXMLTag ()
specifier|public
name|void
name|testTokenizeXMLPairNoXMLTag
parameter_list|()
throws|throws
name|Exception
block|{
name|Expression
name|exp
init|=
name|TokenizeLanguage
operator|.
name|tokenizeXML
argument_list|(
literal|"person"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"<persons><person>James</person><person>Claus</person><person>Jonathan</person><person>Hadrian</person></persons>"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|names
init|=
name|exp
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|names
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<person>James</person>"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<person>Claus</person>"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<person>Jonathan</person>"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<person>Hadrian</person>"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testTokenizeXMLPairWithNoise ()
specifier|public
name|void
name|testTokenizeXMLPairWithNoise
parameter_list|()
throws|throws
name|Exception
block|{
name|Expression
name|exp
init|=
name|TokenizeLanguage
operator|.
name|tokenizeXML
argument_list|(
literal|"<person>"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"<?xml version=\"1.0\"?><!-- bla bla --><persons>\n<person>James</person>\n<person>Claus</person>\n"
operator|+
literal|"<!-- more bla bla --><person>Jonathan</person>\n<person>Hadrian</person>\n</persons>   "
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|names
init|=
name|exp
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|names
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<person>James</person>"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<person>Claus</person>"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<person>Jonathan</person>"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<person>Hadrian</person>"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testTokenizeXMLPairEmpty ()
specifier|public
name|void
name|testTokenizeXMLPairEmpty
parameter_list|()
throws|throws
name|Exception
block|{
name|Expression
name|exp
init|=
name|TokenizeLanguage
operator|.
name|tokenizeXML
argument_list|(
literal|"<person>"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"<?xml version=\"1.0\"?><!-- bla bla --><persons></persons>   "
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|names
init|=
name|exp
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|names
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testTokenizeXMLPairNoData ()
specifier|public
name|void
name|testTokenizeXMLPairNoData
parameter_list|()
throws|throws
name|Exception
block|{
name|Expression
name|exp
init|=
name|TokenizeLanguage
operator|.
name|tokenizeXML
argument_list|(
literal|"<person>"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|names
init|=
name|exp
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|names
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testTokenizeXMLPairNullData ()
specifier|public
name|void
name|testTokenizeXMLPairNullData
parameter_list|()
throws|throws
name|Exception
block|{
name|Expression
name|exp
init|=
name|TokenizeLanguage
operator|.
name|tokenizeXML
argument_list|(
literal|"<person>"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|names
init|=
name|exp
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|names
argument_list|)
expr_stmt|;
block|}
DECL|method|testTokenizeXMLPairWithSimilarChildNames ()
specifier|public
name|void
name|testTokenizeXMLPairWithSimilarChildNames
parameter_list|()
throws|throws
name|Exception
block|{
name|Expression
name|exp
init|=
name|TokenizeLanguage
operator|.
name|tokenizeXML
argument_list|(
literal|"Trip"
argument_list|,
literal|"Trips"
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"<?xml version='1.0' encoding='UTF-8'?>\n<Trips>\n<Trip>\n<TripType>\n</TripType>\n</Trip>\n</Trips>"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|names
init|=
name|exp
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|names
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testTokenizeXMLPairWithDefaultNamespace ()
specifier|public
name|void
name|testTokenizeXMLPairWithDefaultNamespace
parameter_list|()
throws|throws
name|Exception
block|{
name|Expression
name|exp
init|=
name|TokenizeLanguage
operator|.
name|tokenizeXML
argument_list|(
literal|"<person>"
argument_list|,
literal|"<persons>"
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"<?xml version=\"1.0\"?><persons xmlns=\"http:acme.com/persons\">\n<person>James</person>\n<person>Claus</person>\n"
operator|+
literal|"<person>Jonathan</person>\n<person>Hadrian</person>\n</persons>\n"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|names
init|=
name|exp
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|names
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<person xmlns=\"http:acme.com/persons\">James</person>"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<person xmlns=\"http:acme.com/persons\">Claus</person>"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<person xmlns=\"http:acme.com/persons\">Jonathan</person>"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<person xmlns=\"http:acme.com/persons\">Hadrian</person>"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testTokenizeXMLPairWithDefaultNamespaceNotInherit ()
specifier|public
name|void
name|testTokenizeXMLPairWithDefaultNamespaceNotInherit
parameter_list|()
throws|throws
name|Exception
block|{
name|Expression
name|exp
init|=
name|TokenizeLanguage
operator|.
name|tokenizeXML
argument_list|(
literal|"<person>"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"<?xml version=\"1.0\"?><persons xmlns=\"http:acme.com/persons\">\n<person>James</person>\n<person>Claus</person>\n"
operator|+
literal|"<person>Jonathan</person>\n<person>Hadrian</person>\n</persons>\n"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|names
init|=
name|exp
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|names
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<person>James</person>"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<person>Claus</person>"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<person>Jonathan</person>"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<person>Hadrian</person>"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testTokenizeXMLPairWithDefaultAndFooNamespace ()
specifier|public
name|void
name|testTokenizeXMLPairWithDefaultAndFooNamespace
parameter_list|()
throws|throws
name|Exception
block|{
name|Expression
name|exp
init|=
name|TokenizeLanguage
operator|.
name|tokenizeXML
argument_list|(
literal|"<person>"
argument_list|,
literal|"<persons>"
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"<?xml version=\"1.0\"?><persons xmlns=\"http:acme.com/persons\" xmlns:foo=\"http:foo.com\">\n<person>James</person>\n<person>Claus</person>\n"
operator|+
literal|"<person>Jonathan</person>\n<person>Hadrian</person>\n</persons>\n"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|names
init|=
name|exp
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|names
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<person xmlns=\"http:acme.com/persons\" xmlns:foo=\"http:foo.com\">James</person>"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<person xmlns=\"http:acme.com/persons\" xmlns:foo=\"http:foo.com\">Claus</person>"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<person xmlns=\"http:acme.com/persons\" xmlns:foo=\"http:foo.com\">Jonathan</person>"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<person xmlns=\"http:acme.com/persons\" xmlns:foo=\"http:foo.com\">Hadrian</person>"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testTokenizeXMLPairWithLocalNamespace ()
specifier|public
name|void
name|testTokenizeXMLPairWithLocalNamespace
parameter_list|()
throws|throws
name|Exception
block|{
name|Expression
name|exp
init|=
name|TokenizeLanguage
operator|.
name|tokenizeXML
argument_list|(
literal|"<person>"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"<?xml version=\"1.0\"?><persons>\n<person xmlns=\"http:acme.com/persons\">James</person>\n<person xmlns=\"http:acme.com/persons\">Claus</person>\n"
operator|+
literal|"<person xmlns=\"http:acme.com/persons\">Jonathan</person>\n<person xmlns=\"http:acme.com/persons\">Hadrian</person>\n</persons>\n"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|names
init|=
name|exp
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|names
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<person xmlns=\"http:acme.com/persons\">James</person>"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<person xmlns=\"http:acme.com/persons\">Claus</person>"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<person xmlns=\"http:acme.com/persons\">Jonathan</person>"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<person xmlns=\"http:acme.com/persons\">Hadrian</person>"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testTokenizeXMLPairWithLocalAndInheritedNamespace ()
specifier|public
name|void
name|testTokenizeXMLPairWithLocalAndInheritedNamespace
parameter_list|()
throws|throws
name|Exception
block|{
name|Expression
name|exp
init|=
name|TokenizeLanguage
operator|.
name|tokenizeXML
argument_list|(
literal|"<person>"
argument_list|,
literal|"<persons>"
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"<?xml version=\"1.0\"?><persons xmlns=\"http:acme.com/persons\">\n<person xmlns:foo=\"http:foo.com\">James</person>\n<person>Claus</person>\n"
operator|+
literal|"<person>Jonathan</person>\n<person xmlns:bar=\"http:bar.com\">Hadrian</person>\n</persons>\n"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|names
init|=
name|exp
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|names
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<person xmlns:foo=\"http:foo.com\" xmlns=\"http:acme.com/persons\">James</person>"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<person xmlns=\"http:acme.com/persons\">Claus</person>"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<person xmlns=\"http:acme.com/persons\">Jonathan</person>"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<person xmlns:bar=\"http:bar.com\" xmlns=\"http:acme.com/persons\">Hadrian</person>"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testTokenizeXMLPairWithLocalAndNotInheritedNamespace ()
specifier|public
name|void
name|testTokenizeXMLPairWithLocalAndNotInheritedNamespace
parameter_list|()
throws|throws
name|Exception
block|{
name|Expression
name|exp
init|=
name|TokenizeLanguage
operator|.
name|tokenizeXML
argument_list|(
literal|"<person>"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"<?xml version=\"1.0\"?><persons xmlns=\"http:acme.com/persons\">\n<person xmlns:foo=\"http:foo.com\">James</person>\n"
operator|+
literal|"<person>Claus</person>\n<person>Jonathan</person>\n<person xmlns:bar=\"http:bar.com\">Hadrian</person>\n</persons>\n"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|names
init|=
name|exp
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|names
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<person xmlns:foo=\"http:foo.com\">James</person>"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<person>Claus</person>"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<person>Jonathan</person>"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<person xmlns:bar=\"http:bar.com\">Hadrian</person>"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testTokenizeXMLPairWithAttributes ()
specifier|public
name|void
name|testTokenizeXMLPairWithAttributes
parameter_list|()
throws|throws
name|Exception
block|{
name|Expression
name|exp
init|=
name|TokenizeLanguage
operator|.
name|tokenizeXML
argument_list|(
literal|"<person>"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"<persons><person id=\"1\">James</person><person id=\"2\">Claus</person><person id=\"3\">Jonathan</person>"
operator|+
literal|"<person id=\"4\">Hadrian</person></persons>"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|names
init|=
name|exp
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|names
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<person id=\"1\">James</person>"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<person id=\"2\">Claus</person>"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<person id=\"3\">Jonathan</person>"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<person id=\"4\">Hadrian</person>"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testTokenizeXMLPairWithAttributesInheritNamespace ()
specifier|public
name|void
name|testTokenizeXMLPairWithAttributesInheritNamespace
parameter_list|()
throws|throws
name|Exception
block|{
name|Expression
name|exp
init|=
name|TokenizeLanguage
operator|.
name|tokenizeXML
argument_list|(
literal|"<person>"
argument_list|,
literal|"<persons>"
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"<persons xmlns=\"http:acme.com/persons\"><person id=\"1\">James</person><person id=\"2\">Claus</person>"
operator|+
literal|"<person id=\"3\">Jonathan</person><person id=\"4\">Hadrian</person></persons>"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|names
init|=
name|exp
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|names
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<person id=\"1\" xmlns=\"http:acme.com/persons\">James</person>"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<person id=\"2\" xmlns=\"http:acme.com/persons\">Claus</person>"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<person id=\"3\" xmlns=\"http:acme.com/persons\">Jonathan</person>"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<person id=\"4\" xmlns=\"http:acme.com/persons\">Hadrian</person>"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testTokenizeXMLPairWithAttributes2InheritNamespace ()
specifier|public
name|void
name|testTokenizeXMLPairWithAttributes2InheritNamespace
parameter_list|()
throws|throws
name|Exception
block|{
name|Expression
name|exp
init|=
name|TokenizeLanguage
operator|.
name|tokenizeXML
argument_list|(
literal|"<person>"
argument_list|,
literal|"<persons>"
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"<persons riders=\"true\" xmlns=\"http:acme.com/persons\"><person id=\"1\">James</person><person id=\"2\">Claus</person>"
operator|+
literal|"<person id=\"3\">Jonathan</person><person id=\"4\">Hadrian</person></persons>"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|names
init|=
name|exp
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|names
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<person id=\"1\" xmlns=\"http:acme.com/persons\">James</person>"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<person id=\"2\" xmlns=\"http:acme.com/persons\">Claus</person>"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<person id=\"3\" xmlns=\"http:acme.com/persons\">Jonathan</person>"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<person id=\"4\" xmlns=\"http:acme.com/persons\">Hadrian</person>"
argument_list|,
name|names
operator|.
name|get
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

