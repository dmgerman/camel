begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.cdi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|cdi
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Inject
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
name|EndpointInject
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
name|ProducerTemplate
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
name|builder
operator|.
name|RouteBuilder
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
name|cdi
operator|.
name|ContextName
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
name|cdi
operator|.
name|Uri
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
name|component
operator|.
name|mock
operator|.
name|MockEndpoint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
annotation|@
name|ContextName
argument_list|(
literal|"contextC"
argument_list|)
DECL|class|RoutesContextC
specifier|public
class|class
name|RoutesContextC
extends|extends
name|RouteBuilder
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|RoutesContextC
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|ContextName
argument_list|(
literal|"contextC"
argument_list|)
annotation|@
name|Uri
argument_list|(
literal|"seda:C.a"
argument_list|)
DECL|field|a
name|Endpoint
name|a
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:C.b"
argument_list|,
name|context
operator|=
literal|"contextC"
argument_list|)
DECL|field|b
name|MockEndpoint
name|b
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|ContextName
argument_list|(
literal|"contextC"
argument_list|)
annotation|@
name|Uri
argument_list|(
literal|"seda:C.a"
argument_list|)
DECL|field|producer
name|ProducerTemplate
name|producer
decl_stmt|;
annotation|@
name|Override
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
name|a
argument_list|)
operator|.
name|to
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
DECL|method|sendMessages ()
name|void
name|sendMessages
parameter_list|()
block|{
for|for
control|(
name|Object
name|expectedBody
range|:
name|Constants
operator|.
name|EXPECTED_BODIES_C
control|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Sending "
operator|+
name|expectedBody
operator|+
literal|" to "
operator|+
name|producer
operator|.
name|getDefaultEndpoint
argument_list|()
argument_list|)
expr_stmt|;
name|producer
operator|.
name|sendBody
argument_list|(
name|expectedBody
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

