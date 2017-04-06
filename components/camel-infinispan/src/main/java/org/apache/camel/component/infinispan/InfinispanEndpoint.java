begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.infinispan
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|infinispan
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
name|Consumer
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
name|Processor
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
name|Producer
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
comment|/**  * For reading/writing from/to Infinispan distributed key/value store and data grid.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.13.0"
argument_list|,
name|scheme
operator|=
literal|"infinispan"
argument_list|,
name|title
operator|=
literal|"Infinispan"
argument_list|,
name|syntax
operator|=
literal|"infinispan:cacheName"
argument_list|,
name|consumerClass
operator|=
name|InfinispanConsumer
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"cache,datagrid,clustering"
argument_list|)
DECL|class|InfinispanEndpoint
specifier|public
class|class
name|InfinispanEndpoint
extends|extends
name|DefaultEndpoint
block|{
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"The cache to use"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|cacheName
specifier|private
specifier|final
name|String
name|cacheName
decl_stmt|;
annotation|@
name|UriParam
DECL|field|configuration
specifier|private
specifier|final
name|InfinispanConfiguration
name|configuration
decl_stmt|;
DECL|method|InfinispanEndpoint (String uri, String cacheName, InfinispanComponent component, InfinispanConfiguration configuration)
specifier|public
name|InfinispanEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|cacheName
parameter_list|,
name|InfinispanComponent
name|component
parameter_list|,
name|InfinispanConfiguration
name|configuration
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
name|cacheName
operator|=
name|cacheName
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|InfinispanProducer
argument_list|(
name|this
argument_list|,
name|cacheName
argument_list|,
name|configuration
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|InfinispanConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|,
name|cacheName
argument_list|,
name|configuration
argument_list|)
return|;
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
DECL|method|getCacheName ()
specifier|public
name|String
name|getCacheName
parameter_list|()
block|{
return|return
name|cacheName
return|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|InfinispanConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
block|}
end_class

end_unit

