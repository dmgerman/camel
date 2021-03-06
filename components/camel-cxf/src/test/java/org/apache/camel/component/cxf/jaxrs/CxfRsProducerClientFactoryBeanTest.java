begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.jaxrs
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
operator|.
name|jaxrs
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
name|CamelContext
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
name|SpringCamelContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|interceptor
operator|.
name|Interceptor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|interceptor
operator|.
name|LoggingInInterceptor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|interceptor
operator|.
name|LoggingOutInterceptor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|client
operator|.
name|JAXRSClientFactoryBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

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
name|Before
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
name|AbstractApplicationContext
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
comment|/**  *  */
end_comment

begin_class
DECL|class|CxfRsProducerClientFactoryBeanTest
specifier|public
class|class
name|CxfRsProducerClientFactoryBeanTest
extends|extends
name|Assert
block|{
DECL|field|context
specifier|private
name|CamelContext
name|context
decl_stmt|;
DECL|field|applicationContext
specifier|private
name|AbstractApplicationContext
name|applicationContext
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|applicationContext
operator|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/component/cxf/jaxrs/CxfRsProducerClientFactoryBeanTest.xml"
argument_list|)
expr_stmt|;
name|context
operator|=
name|SpringCamelContext
operator|.
name|springCamelContext
argument_list|(
name|applicationContext
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testProducerInOutInterceptors ()
specifier|public
name|void
name|testProducerInOutInterceptors
parameter_list|()
throws|throws
name|Exception
block|{
name|CxfRsEndpoint
name|e
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"cxfrs://bean://rsClientHttpInterceptors"
argument_list|,
name|CxfRsEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|CxfRsProducer
name|p
init|=
operator|new
name|CxfRsProducer
argument_list|(
name|e
argument_list|)
decl_stmt|;
name|CxfRsProducer
operator|.
name|ClientFactoryBeanCache
name|cache
init|=
name|p
operator|.
name|getClientFactoryBeanCache
argument_list|()
decl_stmt|;
name|JAXRSClientFactoryBean
name|bean
init|=
name|cache
operator|.
name|get
argument_list|(
literal|"http://localhost:8080/CxfRsProducerClientFactoryBeanInterceptors/"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
argument_list|>
argument_list|>
name|ins
init|=
name|bean
operator|.
name|getInInterceptors
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|ins
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ins
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|instanceof
name|LoggingInInterceptor
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
argument_list|>
argument_list|>
name|outs
init|=
name|bean
operator|.
name|getOutInterceptors
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|outs
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|outs
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|instanceof
name|LoggingOutInterceptor
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
comment|// need to shutdown the application context to shutdown the bus
if|if
condition|(
name|applicationContext
operator|!=
literal|null
condition|)
block|{
name|applicationContext
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

