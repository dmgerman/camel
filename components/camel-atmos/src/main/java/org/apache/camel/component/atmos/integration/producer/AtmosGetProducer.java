begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.atmos.integration.producer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|atmos
operator|.
name|integration
operator|.
name|producer
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
name|component
operator|.
name|atmos
operator|.
name|AtmosConfiguration
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
name|atmos
operator|.
name|AtmosEndpoint
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
name|atmos
operator|.
name|core
operator|.
name|AtmosAPIFacade
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
name|atmos
operator|.
name|dto
operator|.
name|AtmosResult
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
DECL|class|AtmosGetProducer
specifier|public
class|class
name|AtmosGetProducer
extends|extends
name|AtmosProducer
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|AtmosGetProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|AtmosGetProducer (AtmosEndpoint endpoint, AtmosConfiguration configuration)
specifier|public
name|AtmosGetProducer
parameter_list|(
name|AtmosEndpoint
name|endpoint
parameter_list|,
name|AtmosConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|AtmosResult
name|result
init|=
name|AtmosAPIFacade
operator|.
name|getInstance
argument_list|(
name|configuration
operator|.
name|getClient
argument_list|()
argument_list|)
operator|.
name|get
argument_list|(
name|configuration
operator|.
name|getRemotePath
argument_list|()
argument_list|)
decl_stmt|;
name|result
operator|.
name|populateExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"producer --> downloaded: "
operator|+
name|result
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

