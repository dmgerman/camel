begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf
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
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|endpoint
operator|.
name|Client
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
name|endpoint
operator|.
name|Server
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
name|frontend
operator|.
name|AbstractWSDLBasedEndpointFactory
import|;
end_import

begin_class
DECL|class|ChainedCxfConfigurer
specifier|public
specifier|final
class|class
name|ChainedCxfConfigurer
implements|implements
name|CxfConfigurer
block|{
DECL|field|parent
specifier|private
name|CxfConfigurer
name|parent
decl_stmt|;
DECL|field|child
specifier|private
name|CxfConfigurer
name|child
decl_stmt|;
DECL|method|ChainedCxfConfigurer ()
specifier|private
name|ChainedCxfConfigurer
parameter_list|()
block|{     }
DECL|method|create (CxfConfigurer parent, CxfConfigurer child)
specifier|public
specifier|static
name|ChainedCxfConfigurer
name|create
parameter_list|(
name|CxfConfigurer
name|parent
parameter_list|,
name|CxfConfigurer
name|child
parameter_list|)
block|{
name|ChainedCxfConfigurer
name|result
init|=
operator|new
name|ChainedCxfConfigurer
argument_list|()
decl_stmt|;
name|result
operator|.
name|parent
operator|=
name|parent
expr_stmt|;
name|result
operator|.
name|child
operator|=
name|child
expr_stmt|;
return|return
name|result
return|;
block|}
DECL|method|addChild (CxfConfigurer cxfConfigurer)
specifier|public
name|ChainedCxfConfigurer
name|addChild
parameter_list|(
name|CxfConfigurer
name|cxfConfigurer
parameter_list|)
block|{
name|ChainedCxfConfigurer
name|result
init|=
operator|new
name|ChainedCxfConfigurer
argument_list|()
decl_stmt|;
name|result
operator|.
name|parent
operator|=
name|this
expr_stmt|;
name|result
operator|.
name|child
operator|=
name|cxfConfigurer
expr_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|Override
DECL|method|configure (AbstractWSDLBasedEndpointFactory factoryBean)
specifier|public
name|void
name|configure
parameter_list|(
name|AbstractWSDLBasedEndpointFactory
name|factoryBean
parameter_list|)
block|{
name|parent
operator|.
name|configure
argument_list|(
name|factoryBean
argument_list|)
expr_stmt|;
name|child
operator|.
name|configure
argument_list|(
name|factoryBean
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|configureClient (Client client)
specifier|public
name|void
name|configureClient
parameter_list|(
name|Client
name|client
parameter_list|)
block|{
name|parent
operator|.
name|configureClient
argument_list|(
name|client
argument_list|)
expr_stmt|;
name|child
operator|.
name|configureClient
argument_list|(
name|client
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|configureServer (Server server)
specifier|public
name|void
name|configureServer
parameter_list|(
name|Server
name|server
parameter_list|)
block|{
name|parent
operator|.
name|configureServer
argument_list|(
name|server
argument_list|)
expr_stmt|;
name|child
operator|.
name|configureServer
argument_list|(
name|server
argument_list|)
expr_stmt|;
block|}
DECL|class|NullCxfConfigurer
specifier|public
specifier|static
class|class
name|NullCxfConfigurer
implements|implements
name|CxfConfigurer
block|{
annotation|@
name|Override
DECL|method|configure (AbstractWSDLBasedEndpointFactory factoryBean)
specifier|public
name|void
name|configure
parameter_list|(
name|AbstractWSDLBasedEndpointFactory
name|factoryBean
parameter_list|)
block|{         }
annotation|@
name|Override
DECL|method|configureClient (Client client)
specifier|public
name|void
name|configureClient
parameter_list|(
name|Client
name|client
parameter_list|)
block|{         }
annotation|@
name|Override
DECL|method|configureServer (Server server)
specifier|public
name|void
name|configureServer
parameter_list|(
name|Server
name|server
parameter_list|)
block|{         }
block|}
block|}
end_class

end_unit

