begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|class|CastUtils
specifier|public
specifier|final
class|class
name|CastUtils
block|{
DECL|method|CastUtils ()
specifier|private
name|CastUtils
parameter_list|()
block|{
comment|//utility class, never constructed
block|}
DECL|method|cast (Map<?, ?> p)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|,
name|U
parameter_list|>
name|Map
argument_list|<
name|T
argument_list|,
name|U
argument_list|>
name|cast
parameter_list|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|p
parameter_list|)
block|{
return|return
operator|(
name|Map
argument_list|<
name|T
argument_list|,
name|U
argument_list|>
operator|)
name|p
return|;
block|}
DECL|method|cast (Map<?, ?> p, Class<T> t, Class<U> u)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|,
name|U
parameter_list|>
name|Map
argument_list|<
name|T
argument_list|,
name|U
argument_list|>
name|cast
parameter_list|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|p
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|t
parameter_list|,
name|Class
argument_list|<
name|U
argument_list|>
name|u
parameter_list|)
block|{
return|return
operator|(
name|Map
argument_list|<
name|T
argument_list|,
name|U
argument_list|>
operator|)
name|p
return|;
block|}
DECL|method|cast (Collection<?> p)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Collection
argument_list|<
name|T
argument_list|>
name|cast
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|p
parameter_list|)
block|{
return|return
operator|(
name|Collection
argument_list|<
name|T
argument_list|>
operator|)
name|p
return|;
block|}
DECL|method|cast (Collection<?> p, Class<T> cls)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Collection
argument_list|<
name|T
argument_list|>
name|cast
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|p
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|)
block|{
return|return
operator|(
name|Collection
argument_list|<
name|T
argument_list|>
operator|)
name|p
return|;
block|}
DECL|method|cast (List<?> p)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|List
argument_list|<
name|T
argument_list|>
name|cast
parameter_list|(
name|List
argument_list|<
name|?
argument_list|>
name|p
parameter_list|)
block|{
return|return
operator|(
name|List
argument_list|<
name|T
argument_list|>
operator|)
name|p
return|;
block|}
DECL|method|cast (List<?> p, Class<T> cls)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|List
argument_list|<
name|T
argument_list|>
name|cast
parameter_list|(
name|List
argument_list|<
name|?
argument_list|>
name|p
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|)
block|{
return|return
operator|(
name|List
argument_list|<
name|T
argument_list|>
operator|)
name|p
return|;
block|}
DECL|method|cast (Iterator<?> p)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Iterator
argument_list|<
name|T
argument_list|>
name|cast
parameter_list|(
name|Iterator
argument_list|<
name|?
argument_list|>
name|p
parameter_list|)
block|{
return|return
operator|(
name|Iterator
argument_list|<
name|T
argument_list|>
operator|)
name|p
return|;
block|}
DECL|method|cast (Iterator<?> p, Class<T> cls)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Iterator
argument_list|<
name|T
argument_list|>
name|cast
parameter_list|(
name|Iterator
argument_list|<
name|?
argument_list|>
name|p
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|)
block|{
return|return
operator|(
name|Iterator
argument_list|<
name|T
argument_list|>
operator|)
name|p
return|;
block|}
DECL|method|cast (Set<?> p)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Set
argument_list|<
name|T
argument_list|>
name|cast
parameter_list|(
name|Set
argument_list|<
name|?
argument_list|>
name|p
parameter_list|)
block|{
return|return
operator|(
name|Set
argument_list|<
name|T
argument_list|>
operator|)
name|p
return|;
block|}
DECL|method|cast (Set<?> p, Class<T> cls)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Set
argument_list|<
name|T
argument_list|>
name|cast
parameter_list|(
name|Set
argument_list|<
name|?
argument_list|>
name|p
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|)
block|{
return|return
operator|(
name|Set
argument_list|<
name|T
argument_list|>
operator|)
name|p
return|;
block|}
DECL|method|cast (Map.Entry<?, ?> p)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|,
name|U
parameter_list|>
name|Map
operator|.
name|Entry
argument_list|<
name|T
argument_list|,
name|U
argument_list|>
name|cast
parameter_list|(
name|Map
operator|.
name|Entry
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|p
parameter_list|)
block|{
return|return
operator|(
name|Map
operator|.
name|Entry
argument_list|<
name|T
argument_list|,
name|U
argument_list|>
operator|)
name|p
return|;
block|}
DECL|method|cast (Map.Entry<?, ?> p, Class<T> pc, Class<U> uc)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|,
name|U
parameter_list|>
name|Map
operator|.
name|Entry
argument_list|<
name|T
argument_list|,
name|U
argument_list|>
name|cast
parameter_list|(
name|Map
operator|.
name|Entry
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|p
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|pc
parameter_list|,
name|Class
argument_list|<
name|U
argument_list|>
name|uc
parameter_list|)
block|{
return|return
operator|(
name|Map
operator|.
name|Entry
argument_list|<
name|T
argument_list|,
name|U
argument_list|>
operator|)
name|p
return|;
block|}
block|}
end_class

end_unit

