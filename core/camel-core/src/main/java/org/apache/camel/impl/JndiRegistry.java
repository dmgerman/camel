begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|Context
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|NamingException
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
name|spi
operator|.
name|Registry
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
name|jndi
operator|.
name|JndiBeanRepository
import|;
end_import

begin_comment
comment|/**  * A {@link Registry} implementation which looks up the objects in JNDI  *  * @deprecated use {@link JndiBeanRepository} instead.  */
end_comment

begin_class
annotation|@
name|Deprecated
DECL|class|JndiRegistry
specifier|public
class|class
name|JndiRegistry
extends|extends
name|JndiBeanRepository
implements|implements
name|Registry
block|{
DECL|method|JndiRegistry ()
specifier|public
name|JndiRegistry
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
DECL|method|JndiRegistry (Map environment)
specifier|public
name|JndiRegistry
parameter_list|(
name|Map
name|environment
parameter_list|)
block|{
name|super
argument_list|(
name|environment
argument_list|)
expr_stmt|;
block|}
DECL|method|JndiRegistry (Context context)
specifier|public
name|JndiRegistry
parameter_list|(
name|Context
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
DECL|method|JndiRegistry (boolean standalone)
specifier|public
name|JndiRegistry
parameter_list|(
name|boolean
name|standalone
parameter_list|)
block|{
name|super
argument_list|(
name|standalone
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|bind (String id, Class<?> type, Object bean)
specifier|public
name|void
name|bind
parameter_list|(
name|String
name|id
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|Object
name|bean
parameter_list|)
throws|throws
name|RuntimeCamelException
block|{
try|try
block|{
name|Object
name|object
init|=
name|wrap
argument_list|(
name|bean
argument_list|)
decl_stmt|;
name|getContext
argument_list|()
operator|.
name|bind
argument_list|(
name|id
argument_list|,
name|object
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NamingException
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

