begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.undertow
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|undertow
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Objects
import|;
end_import

begin_class
DECL|class|HttpHandlerRegistrationInfo
specifier|public
class|class
name|HttpHandlerRegistrationInfo
block|{
DECL|field|matchOnUriPrefix
specifier|private
specifier|final
name|Boolean
name|matchOnUriPrefix
decl_stmt|;
DECL|field|methodRestrict
specifier|private
specifier|final
name|String
name|methodRestrict
decl_stmt|;
DECL|field|uri
specifier|private
specifier|final
name|URI
name|uri
decl_stmt|;
DECL|method|HttpHandlerRegistrationInfo (URI uri, String methodRestrict, Boolean matchOnUriPrefix)
specifier|public
name|HttpHandlerRegistrationInfo
parameter_list|(
name|URI
name|uri
parameter_list|,
name|String
name|methodRestrict
parameter_list|,
name|Boolean
name|matchOnUriPrefix
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|matchOnUriPrefix
operator|=
name|matchOnUriPrefix
expr_stmt|;
name|this
operator|.
name|methodRestrict
operator|=
name|methodRestrict
expr_stmt|;
name|this
operator|.
name|uri
operator|=
name|uri
expr_stmt|;
block|}
DECL|method|getMethodRestrict ()
specifier|public
name|String
name|getMethodRestrict
parameter_list|()
block|{
return|return
name|methodRestrict
return|;
block|}
DECL|method|getUri ()
specifier|public
name|URI
name|getUri
parameter_list|()
block|{
return|return
name|uri
return|;
block|}
DECL|method|isMatchOnUriPrefix ()
specifier|public
name|Boolean
name|isMatchOnUriPrefix
parameter_list|()
block|{
return|return
name|matchOnUriPrefix
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|uri
operator|+
literal|"?matchOnUriPrefix="
operator|+
name|matchOnUriPrefix
operator|+
literal|"&methodRestrict="
operator|+
name|methodRestrict
return|;
block|}
annotation|@
name|Override
DECL|method|equals (Object o)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|o
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|o
operator|==
literal|null
operator|||
name|getClass
argument_list|()
operator|!=
name|o
operator|.
name|getClass
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|HttpHandlerRegistrationInfo
name|that
init|=
operator|(
name|HttpHandlerRegistrationInfo
operator|)
name|o
decl_stmt|;
return|return
name|Objects
operator|.
name|equals
argument_list|(
name|matchOnUriPrefix
argument_list|,
name|that
operator|.
name|matchOnUriPrefix
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|methodRestrict
argument_list|,
name|that
operator|.
name|methodRestrict
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|uri
argument_list|,
name|that
operator|.
name|uri
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|Objects
operator|.
name|hash
argument_list|(
name|matchOnUriPrefix
argument_list|,
name|methodRestrict
argument_list|,
name|uri
argument_list|)
return|;
block|}
block|}
end_class

end_unit

