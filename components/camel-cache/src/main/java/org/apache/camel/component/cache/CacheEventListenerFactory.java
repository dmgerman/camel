begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cache
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cache
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_class
DECL|class|CacheEventListenerFactory
specifier|public
class|class
name|CacheEventListenerFactory
extends|extends
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|event
operator|.
name|CacheEventListenerFactory
block|{
DECL|method|createCacheEventListener (Properties properties)
specifier|public
name|CacheEventListener
name|createCacheEventListener
parameter_list|(
name|Properties
name|properties
parameter_list|)
block|{
return|return
operator|new
name|CacheEventListener
argument_list|()
return|;
block|}
block|}
end_class

end_unit

