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

begin_class
DECL|class|EtcdWatchEndpoint
specifier|public
class|class
name|EtcdWatchEndpoint
extends|extends
name|AbstractEtcdEndpoint
block|{
DECL|method|EtcdWatchEndpoint ( String uri, EtcdComponent component, EtcdConfiguration configuration, EtcdNamespace namespace, String path)
specifier|public
name|EtcdWatchEndpoint
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
argument_list|,
name|configuration
argument_list|,
name|namespace
argument_list|,
name|path
argument_list|)
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
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Producer not enabled for "
operator|+
name|getPath
argument_list|()
argument_list|)
throw|;
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
name|EtcdWatchConsumer
name|consumer
init|=
operator|new
name|EtcdWatchConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|,
name|getConfiguration
argument_list|()
argument_list|,
name|getNamespace
argument_list|()
argument_list|,
name|getPath
argument_list|()
argument_list|)
decl_stmt|;
name|configureConsumer
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
return|return
name|consumer
return|;
block|}
block|}
end_class

end_unit

