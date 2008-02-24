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
name|Hashtable
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
name|InitialContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|NameNotFoundException
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

begin_comment
comment|/**  * A {@link Registry} implementation which looks up the objects in JNDI  *   * @version $Revision$  */
end_comment

begin_class
DECL|class|JndiRegistry
specifier|public
class|class
name|JndiRegistry
implements|implements
name|Registry
block|{
DECL|field|context
specifier|private
name|Context
name|context
decl_stmt|;
DECL|method|JndiRegistry ()
specifier|public
name|JndiRegistry
parameter_list|()
block|{     }
DECL|method|JndiRegistry (Context context)
specifier|public
name|JndiRegistry
parameter_list|(
name|Context
name|context
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
block|}
DECL|method|lookup (String name, Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|lookup
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
name|Object
name|value
init|=
name|lookup
argument_list|(
name|name
argument_list|)
decl_stmt|;
return|return
name|type
operator|.
name|cast
argument_list|(
name|value
argument_list|)
return|;
block|}
DECL|method|lookup (String name)
specifier|public
name|Object
name|lookup
parameter_list|(
name|String
name|name
parameter_list|)
block|{
try|try
block|{
return|return
name|getContext
argument_list|()
operator|.
name|lookup
argument_list|(
name|name
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NameNotFoundException
name|e
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
catch|catch
parameter_list|(
name|NamingException
name|e
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
DECL|method|bind (String s, Object o)
specifier|public
name|void
name|bind
parameter_list|(
name|String
name|s
parameter_list|,
name|Object
name|o
parameter_list|)
block|{
try|try
block|{
name|getContext
argument_list|()
operator|.
name|bind
argument_list|(
name|s
argument_list|,
name|o
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
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|NamingException
block|{
name|getContext
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
DECL|method|getContext ()
specifier|public
name|Context
name|getContext
parameter_list|()
throws|throws
name|NamingException
block|{
if|if
condition|(
name|context
operator|==
literal|null
condition|)
block|{
name|context
operator|=
name|createContext
argument_list|()
expr_stmt|;
block|}
return|return
name|context
return|;
block|}
DECL|method|setContext (Context context)
specifier|public
name|void
name|setContext
parameter_list|(
name|Context
name|context
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
block|}
DECL|method|createContext ()
specifier|protected
name|Context
name|createContext
parameter_list|()
throws|throws
name|NamingException
block|{
name|Hashtable
name|properties
init|=
operator|new
name|Hashtable
argument_list|(
name|System
operator|.
name|getProperties
argument_list|()
argument_list|)
decl_stmt|;
return|return
operator|new
name|InitialContext
argument_list|(
name|properties
argument_list|)
return|;
block|}
block|}
end_class

end_unit

