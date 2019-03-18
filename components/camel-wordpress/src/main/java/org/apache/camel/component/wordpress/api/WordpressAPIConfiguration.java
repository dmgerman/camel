begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.wordpress.api
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|wordpress
operator|.
name|api
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
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

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|Objects
operator|.
name|hash
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
name|wordpress
operator|.
name|api
operator|.
name|auth
operator|.
name|WordpressAuthentication
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|MoreObjects
operator|.
name|toStringHelper
import|;
end_import

begin_comment
comment|/**  * Model for the API configuration.  */
end_comment

begin_class
DECL|class|WordpressAPIConfiguration
specifier|public
specifier|final
class|class
name|WordpressAPIConfiguration
implements|implements
name|Serializable
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|3512991364074374129L
decl_stmt|;
DECL|field|apiUrl
specifier|private
name|String
name|apiUrl
decl_stmt|;
DECL|field|apiVersion
specifier|private
name|String
name|apiVersion
decl_stmt|;
DECL|field|authentication
specifier|private
name|WordpressAuthentication
name|authentication
decl_stmt|;
DECL|method|WordpressAPIConfiguration ()
specifier|public
name|WordpressAPIConfiguration
parameter_list|()
block|{      }
DECL|method|WordpressAPIConfiguration (final String apiUrl, final String apiVersion)
specifier|public
name|WordpressAPIConfiguration
parameter_list|(
specifier|final
name|String
name|apiUrl
parameter_list|,
specifier|final
name|String
name|apiVersion
parameter_list|)
block|{
name|this
operator|.
name|apiUrl
operator|=
name|apiUrl
expr_stmt|;
name|this
operator|.
name|apiVersion
operator|=
name|apiVersion
expr_stmt|;
block|}
DECL|method|getApiUrl ()
specifier|public
name|String
name|getApiUrl
parameter_list|()
block|{
return|return
name|apiUrl
return|;
block|}
DECL|method|setApiUrl (String apiUrl)
specifier|public
name|void
name|setApiUrl
parameter_list|(
name|String
name|apiUrl
parameter_list|)
block|{
name|this
operator|.
name|apiUrl
operator|=
name|apiUrl
expr_stmt|;
block|}
DECL|method|getApiVersion ()
specifier|public
name|String
name|getApiVersion
parameter_list|()
block|{
return|return
name|apiVersion
return|;
block|}
DECL|method|setApiVersion (String apiVersion)
specifier|public
name|void
name|setApiVersion
parameter_list|(
name|String
name|apiVersion
parameter_list|)
block|{
name|this
operator|.
name|apiVersion
operator|=
name|apiVersion
expr_stmt|;
block|}
DECL|method|getAuthentication ()
specifier|public
name|WordpressAuthentication
name|getAuthentication
parameter_list|()
block|{
return|return
name|authentication
return|;
block|}
DECL|method|setAuthentication (WordpressAuthentication authentication)
specifier|public
name|void
name|setAuthentication
parameter_list|(
name|WordpressAuthentication
name|authentication
parameter_list|)
block|{
name|this
operator|.
name|authentication
operator|=
name|authentication
expr_stmt|;
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
name|toStringHelper
argument_list|(
name|this
argument_list|)
operator|.
name|addValue
argument_list|(
name|this
operator|.
name|apiUrl
argument_list|)
operator|.
name|add
argument_list|(
literal|"Version"
argument_list|,
name|this
operator|.
name|apiVersion
argument_list|)
operator|.
name|addValue
argument_list|(
name|this
operator|.
name|authentication
argument_list|)
operator|.
name|toString
argument_list|()
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
name|hash
argument_list|(
name|this
operator|.
name|apiUrl
argument_list|,
name|this
operator|.
name|apiVersion
argument_list|,
name|this
operator|.
name|authentication
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|equals (Object obj)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|obj
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
operator|!
name|WordpressAPIConfiguration
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|obj
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|Objects
operator|.
name|equals
argument_list|(
name|this
argument_list|,
name|obj
argument_list|)
return|;
block|}
block|}
end_class

end_unit

