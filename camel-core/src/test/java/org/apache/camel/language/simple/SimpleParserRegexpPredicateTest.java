begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.language.simple
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
operator|.
name|simple
package|;
end_package

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
name|Predicate
import|;
end_import

begin_comment
comment|/**  * Unit test regexp function as the reg exp value should be template text only  * and not any embedded functions etc.  */
end_comment

begin_class
DECL|class|SimpleParserRegexpPredicateTest
specifier|public
class|class
name|SimpleParserRegexpPredicateTest
extends|extends
name|ExchangeTestSupport
block|{
DECL|method|testSimpleRegexp ()
specifier|public
name|void
name|testSimpleRegexp
parameter_list|()
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"12.34.5678"
argument_list|)
expr_stmt|;
name|SimplePredicateParser
name|parser
init|=
operator|new
name|SimplePredicateParser
argument_list|(
literal|"${body} regex '^\\d{2}\\.\\d{2}\\.\\d{4}$'"
argument_list|)
decl_stmt|;
name|Predicate
name|pre
init|=
name|parser
operator|.
name|parsePredicate
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|pre
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

