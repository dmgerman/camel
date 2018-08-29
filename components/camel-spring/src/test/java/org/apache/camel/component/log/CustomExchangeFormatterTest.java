begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.log
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|log
package|;
end_package

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
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
name|Endpoint
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
name|spring
operator|.
name|SpringTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|AbstractXmlApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|ClassPathXmlApplicationContext
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|CustomExchangeFormatterTest
specifier|public
class|class
name|CustomExchangeFormatterTest
extends|extends
name|SpringTestSupport
block|{
annotation|@
name|Test
DECL|method|testExchangeFormattersConfiguredProperly ()
specifier|public
name|void
name|testExchangeFormattersConfiguredProperly
parameter_list|()
throws|throws
name|Exception
block|{
name|TestExchangeFormatter
name|aaa
init|=
literal|null
decl_stmt|;
name|TestExchangeFormatter
name|bbb
init|=
literal|null
decl_stmt|;
for|for
control|(
name|Endpoint
name|ep
range|:
name|context
operator|.
name|getEndpoints
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
operator|(
name|ep
operator|instanceof
name|LogEndpoint
operator|)
condition|)
block|{
continue|continue;
block|}
name|LogEndpoint
name|log
init|=
operator|(
name|LogEndpoint
operator|)
name|ep
decl_stmt|;
name|aaa
operator|=
literal|"aaa"
operator|.
name|equals
argument_list|(
name|log
operator|.
name|getLoggerName
argument_list|()
argument_list|)
condition|?
operator|(
name|TestExchangeFormatter
operator|)
name|log
operator|.
name|getLocalFormatter
argument_list|()
else|:
name|aaa
expr_stmt|;
name|bbb
operator|=
literal|"bbb"
operator|.
name|equals
argument_list|(
name|log
operator|.
name|getLoggerName
argument_list|()
argument_list|)
condition|?
operator|(
name|TestExchangeFormatter
operator|)
name|log
operator|.
name|getLocalFormatter
argument_list|()
else|:
name|bbb
expr_stmt|;
block|}
name|assertNotNull
argument_list|(
name|aaa
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|bbb
argument_list|)
expr_stmt|;
name|assertNotSame
argument_list|(
name|aaa
argument_list|,
name|bbb
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"aaa"
argument_list|,
name|aaa
operator|.
name|getTestProperty
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bbb"
argument_list|,
name|bbb
operator|.
name|getTestProperty
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|createApplicationContext ()
specifier|protected
name|AbstractXmlApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/component/log/custom-exchange-formatter-context.xml"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

