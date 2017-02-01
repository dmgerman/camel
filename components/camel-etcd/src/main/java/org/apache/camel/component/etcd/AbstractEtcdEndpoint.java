begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|impl
operator|.
name|DefaultEndpoint
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
name|spi
operator|.
name|Metadata
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
name|spi
operator|.
name|UriEndpoint
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
name|spi
operator|.
name|UriParam
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
name|spi
operator|.
name|UriPath
import|;
end_import

begin_comment
comment|/**  * The camel etcd component allows you to work with<a href="https://coreos.com/etcd">Etcd</a>, a distributed, A distributed, reliable key-value store.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.18.0"
argument_list|,
name|scheme
operator|=
literal|"etcd"
argument_list|,
name|title
operator|=
literal|"etcd"
argument_list|,
name|syntax
operator|=
literal|"etcd:namespace/path"
argument_list|,
name|consumerClass
operator|=
name|AbstractEtcdConsumer
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"etcd"
argument_list|)
DECL|class|AbstractEtcdEndpoint
specifier|public
specifier|abstract
class|class
name|AbstractEtcdEndpoint
extends|extends
name|DefaultEndpoint
implements|implements
name|EtcdEndpoint
block|{
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"The API namespace to use"
argument_list|,
name|enums
operator|=
literal|"keys,stats,watch"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|namespace
specifier|private
specifier|final
name|EtcdNamespace
name|namespace
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"The path the enpoint refers to"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"false"
argument_list|)
DECL|field|path
specifier|private
specifier|final
name|String
name|path
decl_stmt|;
annotation|@
name|UriParam
DECL|field|configuration
specifier|private
specifier|final
name|EtcdConfiguration
name|configuration
decl_stmt|;
DECL|method|AbstractEtcdEndpoint (String uri, EtcdComponent component, EtcdConfiguration configuration, EtcdNamespace namespace, String path)
specifier|protected
name|AbstractEtcdEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|EtcdComponent
name|component
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
name|uri
argument_list|,
name|component
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
block|}
annotation|@
name|Override
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|getConfiguration ()
specifier|public
name|EtcdConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|this
operator|.
name|configuration
return|;
block|}
annotation|@
name|Override
DECL|method|getNamespace ()
specifier|public
name|EtcdNamespace
name|getNamespace
parameter_list|()
block|{
return|return
name|this
operator|.
name|namespace
return|;
block|}
annotation|@
name|Override
DECL|method|getPath ()
specifier|public
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
annotation|@
name|Override
DECL|method|createClient ()
specifier|public
name|EtcdClient
name|createClient
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|configuration
operator|.
name|createClient
argument_list|()
return|;
block|}
block|}
end_class

end_unit

