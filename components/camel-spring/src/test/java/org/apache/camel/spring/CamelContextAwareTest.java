begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|CamelContextAwareTest
specifier|public
class|class
name|CamelContextAwareTest
extends|extends
name|SpringTestSupport
block|{
DECL|field|bean
specifier|protected
name|CamelContextAwareBean
name|bean
decl_stmt|;
DECL|method|testInjectionPoints ()
specifier|public
name|void
name|testInjectionPoints
parameter_list|()
throws|throws
name|Exception
block|{
name|assertNotNull
argument_list|(
literal|"No CamelContext injected!"
argument_list|,
name|bean
operator|.
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|properties
init|=
name|bean
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getProperties
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"the properties should not been null"
argument_list|,
name|properties
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"No properties injected"
argument_list|,
name|properties
operator|.
name|size
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should get the value of org.apache.camel.test"
argument_list|,
name|properties
operator|.
name|get
argument_list|(
literal|"org.apache.camel.test"
argument_list|)
argument_list|,
literal|"this is a test"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|bean
operator|=
name|getMandatoryBean
argument_list|(
name|CamelContextAwareBean
operator|.
name|class
argument_list|,
literal|"bean"
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
literal|"org/apache/camel/spring/camelContextAwareBean.xml"
argument_list|)
return|;
block|}
DECL|method|getExpectedRouteCount ()
specifier|protected
name|int
name|getExpectedRouteCount
parameter_list|()
block|{
return|return
literal|0
return|;
block|}
block|}
end_class

end_unit

