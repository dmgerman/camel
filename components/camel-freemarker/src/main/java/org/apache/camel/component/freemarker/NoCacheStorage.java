begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.freemarker
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|freemarker
package|;
end_package

begin_import
import|import
name|freemarker
operator|.
name|cache
operator|.
name|CacheStorage
import|;
end_import

begin_comment
comment|/**  * A cache storage for Freemarker with no cache used for development to force reload of templates  * on every request.  */
end_comment

begin_class
DECL|class|NoCacheStorage
specifier|public
class|class
name|NoCacheStorage
implements|implements
name|CacheStorage
block|{
DECL|method|get (Object key)
specifier|public
name|Object
name|get
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
comment|// noop
return|return
literal|null
return|;
block|}
DECL|method|put (Object key, Object value)
specifier|public
name|void
name|put
parameter_list|(
name|Object
name|key
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
comment|// noop
block|}
DECL|method|remove (Object key)
specifier|public
name|void
name|remove
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
comment|// noop
block|}
DECL|method|clear ()
specifier|public
name|void
name|clear
parameter_list|()
block|{
comment|// noop
block|}
block|}
end_class

end_unit

