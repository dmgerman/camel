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
comment|/**  * @version $Revision: $  */
end_comment

begin_class
DECL|class|InjectedBeanTest
specifier|public
class|class
name|InjectedBeanTest
extends|extends
name|SpringTestSupport
block|{
DECL|field|bean
specifier|protected
name|InjectedBean
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
name|log
operator|.
name|info
argument_list|(
literal|"getFieldInjectedEndpoint()         = "
operator|+
name|bean
operator|.
name|getFieldInjectedEndpoint
argument_list|()
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"getPropertyInjectedEndpoint()      = "
operator|+
name|bean
operator|.
name|getPropertyInjectedEndpoint
argument_list|()
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"getFieldInjectedProducer()         = "
operator|+
name|bean
operator|.
name|getFieldInjectedProducer
argument_list|()
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"getPropertyInjectedProducer()      = "
operator|+
name|bean
operator|.
name|getPropertyInjectedProducer
argument_list|()
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"getFieldInjectedCamelTemplate()    = "
operator|+
name|bean
operator|.
name|getFieldInjectedCamelTemplate
argument_list|()
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"getPropertyInjectedCamelTemplate() = "
operator|+
name|bean
operator|.
name|getPropertyInjectedCamelTemplate
argument_list|()
argument_list|)
expr_stmt|;
name|assertEndpointUri
argument_list|(
name|bean
operator|.
name|getFieldInjectedEndpoint
argument_list|()
argument_list|,
literal|"direct:fieldInjectedEndpoint"
argument_list|)
expr_stmt|;
name|assertEndpointUri
argument_list|(
name|bean
operator|.
name|getPropertyInjectedEndpoint
argument_list|()
argument_list|,
literal|"direct:namedEndpoint1"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"No Producer injected for getFieldInjectedProducer()"
argument_list|,
name|bean
operator|.
name|getFieldInjectedProducer
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"No Producer injected for getPropertyInjectedProducer()"
argument_list|,
name|bean
operator|.
name|getPropertyInjectedProducer
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"No CamelTemplate injected for getFieldInjectedCamelTemplate()"
argument_list|,
name|bean
operator|.
name|getFieldInjectedCamelTemplate
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"No CamelTemplate injected for getPropertyInjectedCamelTemplate()"
argument_list|,
name|bean
operator|.
name|getPropertyInjectedCamelTemplate
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"No ProducerTemplate injected for getInjectByFieldName()"
argument_list|,
name|bean
operator|.
name|getInjectByFieldName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"No ProducerTemplate injected for getInjectByPropertyName()"
argument_list|,
name|bean
operator|.
name|getInjectByPropertyName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"No PollingConsumer injected for getFieldInjectedPollingConsumer()"
argument_list|,
name|bean
operator|.
name|getFieldInjectedPollingConsumer
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"No PollingConsumer injected for getPropertyInjectedPollingConsumer()"
argument_list|,
name|bean
operator|.
name|getPropertyInjectedPollingConsumer
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testSendAndReceive ()
specifier|public
name|void
name|testSendAndReceive
parameter_list|()
throws|throws
name|Exception
block|{      }
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
name|InjectedBean
operator|.
name|class
argument_list|,
literal|"injectedBean"
argument_list|)
expr_stmt|;
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
literal|"org/apache/camel/spring/injectedBean.xml"
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

