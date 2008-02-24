begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.bean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|bean
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
name|ClassPathXmlApplicationContext
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|BeanRouteTest
specifier|public
class|class
name|BeanRouteTest
extends|extends
name|SpringTestSupport
block|{
DECL|field|body
specifier|protected
name|Object
name|body
init|=
literal|"James"
decl_stmt|;
DECL|method|testSayHello ()
specifier|public
name|void
name|testSayHello
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|value
init|=
name|template
operator|.
name|sendBody
argument_list|(
literal|"bean:myBean?methodName=sayHello"
argument_list|,
name|body
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Returned value"
argument_list|,
literal|"Hello James!"
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
DECL|method|testSayGoodbye ()
specifier|public
name|void
name|testSayGoodbye
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|value
init|=
name|template
operator|.
name|sendBody
argument_list|(
literal|"bean:myBean?methodName=sayGoodbye"
argument_list|,
name|body
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Returned value"
argument_list|,
literal|"Bye James!"
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
DECL|method|testChooseMethodUsingBodyType ()
specifier|public
name|void
name|testChooseMethodUsingBodyType
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|value
init|=
name|template
operator|.
name|sendBody
argument_list|(
literal|"bean:myBean"
argument_list|,
literal|4
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Returned value"
argument_list|,
literal|8L
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
DECL|method|testAmbiguousMethodCallFails ()
specifier|public
name|void
name|testAmbiguousMethodCallFails
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|Object
name|value
init|=
name|template
operator|.
name|sendBody
argument_list|(
literal|"bean:myBean"
argument_list|,
name|body
argument_list|)
decl_stmt|;
name|fail
argument_list|(
literal|"We should have failed to invoke an ambiguous method but instead got: "
operator|+
name|value
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Caught expected failure: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getExpectedRouteCount ()
specifier|protected
name|int
name|getExpectedRouteCount
parameter_list|()
block|{
comment|// TODO why zero?
return|return
literal|0
return|;
block|}
DECL|method|createApplicationContext ()
specifier|protected
name|ClassPathXmlApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/component/bean/camelContext.xml"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

