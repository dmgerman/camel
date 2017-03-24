begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.viwilo.camel.producer
package|package
name|com
operator|.
name|viwilo
operator|.
name|camel
operator|.
name|producer
package|;
end_package

begin_import
import|import
name|com
operator|.
name|myjeeva
operator|.
name|digitalocean
operator|.
name|pojo
operator|.
name|*
import|;
end_import

begin_import
import|import
name|com
operator|.
name|viwilo
operator|.
name|camel
operator|.
name|DigitalOceanConfiguration
import|;
end_import

begin_import
import|import
name|com
operator|.
name|viwilo
operator|.
name|camel
operator|.
name|DigitalOceanEndpoint
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

begin_comment
comment|/**  * The DigitalOcean producer for Account API.  */
end_comment

begin_class
DECL|class|DigitalOceanAccountProducer
specifier|public
class|class
name|DigitalOceanAccountProducer
extends|extends
name|DigitalOceanProducer
block|{
DECL|method|DigitalOceanAccountProducer (DigitalOceanEndpoint endpoint, DigitalOceanConfiguration configuration)
specifier|public
name|DigitalOceanAccountProducer
parameter_list|(
name|DigitalOceanEndpoint
name|endpoint
parameter_list|,
name|DigitalOceanConfiguration
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
name|Account
name|accountInfo
init|=
name|getEndpoint
argument_list|()
operator|.
name|getDigitalOceanClient
argument_list|()
operator|.
name|getAccountInfo
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Account [{}] "
argument_list|,
name|accountInfo
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|accountInfo
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

