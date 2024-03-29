begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.etcd
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|etcd
package|;
end_package

begin_import
import|import
name|mousio
operator|.
name|etcd4j
operator|.
name|EtcdClient
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
name|support
operator|.
name|DefaultProducer
import|;
end_import

begin_comment
comment|/**  * The etcd producer.  */
end_comment

begin_class
DECL|class|AbstractEtcdProducer
specifier|public
specifier|abstract
class|class
name|AbstractEtcdProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|configuration
specifier|private
specifier|final
name|EtcdConfiguration
name|configuration
decl_stmt|;
DECL|field|namespace
specifier|private
specifier|final
name|EtcdNamespace
name|namespace
decl_stmt|;
DECL|field|path
specifier|private
specifier|final
name|String
name|path
decl_stmt|;
DECL|field|client
specifier|private
name|EtcdClient
name|client
decl_stmt|;
DECL|method|AbstractEtcdProducer (Endpoint endpoint, EtcdConfiguration configuration, EtcdNamespace namespace, String path)
specifier|protected
name|AbstractEtcdProducer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|EtcdConfiguration
name|configuration
parameter_list|,
name|EtcdNamespace
name|namespace
parameter_list|,
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
name|this
operator|.
name|namespace
operator|=
name|namespace
expr_stmt|;
name|this
operator|.
name|path
operator|=
name|path
expr_stmt|;
name|this
operator|.
name|client
operator|=
literal|null
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|client
operator|!=
literal|null
condition|)
block|{
name|client
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
DECL|method|getClient ()
specifier|protected
name|EtcdClient
name|getClient
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|client
operator|==
literal|null
condition|)
block|{
name|client
operator|=
operator|(
operator|(
name|EtcdEndpoint
operator|)
name|getEndpoint
argument_list|()
operator|)
operator|.
name|createClient
argument_list|()
expr_stmt|;
block|}
return|return
name|client
return|;
block|}
DECL|method|getConfiguration ()
specifier|protected
name|EtcdConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|getNamespace ()
specifier|protected
name|EtcdNamespace
name|getNamespace
parameter_list|()
block|{
return|return
name|namespace
return|;
block|}
DECL|method|getPath ()
specifier|protected
name|String
name|getPath
parameter_list|()
block|{
return|return
name|this
operator|.
name|path
return|;
block|}
block|}
end_class

end_unit

