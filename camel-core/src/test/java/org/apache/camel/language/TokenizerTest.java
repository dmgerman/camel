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
block|}
end_class

end_unit

