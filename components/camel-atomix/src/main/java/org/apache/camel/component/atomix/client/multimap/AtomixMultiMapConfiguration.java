begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.atomix.client.multimap
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|atomix
operator|.
name|client
operator|.
name|multimap
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
name|RuntimeCamelException
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
name|atomix
operator|.
name|client
operator|.
name|AtomixClientConfiguration
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
name|UriParams
import|;
end_import

begin_class
annotation|@
name|UriParams
DECL|class|AtomixMultiMapConfiguration
specifier|public
class|class
name|AtomixMultiMapConfiguration
extends|extends
name|AtomixClientConfiguration
block|{
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"PUT"
argument_list|)
DECL|field|defaultAction
specifier|private
name|AtomixMultiMap
operator|.
name|Action
name|defaultAction
init|=
name|AtomixMultiMap
operator|.
name|Action
operator|.
name|PUT
decl_stmt|;
annotation|@
name|UriParam
DECL|field|key
specifier|private
name|Object
name|key
decl_stmt|;
annotation|@
name|UriParam
DECL|field|ttl
specifier|private
name|long
name|ttl
decl_stmt|;
comment|// ****************************************
comment|// Properties
comment|// ****************************************
DECL|method|getDefaultAction ()
specifier|public
name|AtomixMultiMap
operator|.
name|Action
name|getDefaultAction
parameter_list|()
block|{
return|return
name|defaultAction
return|;
block|}
comment|/**      * The default action.      */
DECL|method|setDefaultAction (AtomixMultiMap.Action defaultAction)
specifier|public
name|void
name|setDefaultAction
parameter_list|(
name|AtomixMultiMap
operator|.
name|Action
name|defaultAction
parameter_list|)
block|{
name|this
operator|.
name|defaultAction
operator|=
name|defaultAction
expr_stmt|;
block|}
DECL|method|getKey ()
specifier|public
name|Object
name|getKey
parameter_list|()
block|{
return|return
name|key
return|;
block|}
comment|/**      * The key to use if none is set in the header or to listen for events for      * a specific key.      */
DECL|method|setKey (Object defaultKey)
specifier|public
name|void
name|setKey
parameter_list|(
name|Object
name|defaultKey
parameter_list|)
block|{
name|this
operator|.
name|key
operator|=
name|defaultKey
expr_stmt|;
block|}
DECL|method|getTtl ()
specifier|public
name|long
name|getTtl
parameter_list|()
block|{
return|return
name|ttl
return|;
block|}
comment|/**      * The resource ttl.      */
DECL|method|setTtl (long ttl)
specifier|public
name|void
name|setTtl
parameter_list|(
name|long
name|ttl
parameter_list|)
block|{
name|this
operator|.
name|ttl
operator|=
name|ttl
expr_stmt|;
block|}
comment|// ****************************************
comment|// Copy
comment|// ****************************************
DECL|method|copy ()
specifier|public
name|AtomixMultiMapConfiguration
name|copy
parameter_list|()
block|{
try|try
block|{
return|return
operator|(
name|AtomixMultiMapConfiguration
operator|)
name|super
operator|.
name|clone
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|CloneNotSupportedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

