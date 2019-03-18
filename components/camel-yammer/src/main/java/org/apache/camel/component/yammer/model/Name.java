begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.yammer.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|yammer
operator|.
name|model
package|;
end_package

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|jackson
operator|.
name|annotate
operator|.
name|JsonIgnoreProperties
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|jackson
operator|.
name|annotate
operator|.
name|JsonProperty
import|;
end_import

begin_class
annotation|@
name|JsonIgnoreProperties
argument_list|(
name|ignoreUnknown
operator|=
literal|true
argument_list|)
DECL|class|Name
specifier|public
class|class
name|Name
block|{
DECL|field|permalink
specifier|private
name|String
name|permalink
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"full_name"
argument_list|)
DECL|field|fullName
specifier|private
name|String
name|fullName
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"user_id"
argument_list|)
DECL|field|userId
specifier|private
name|Long
name|userId
decl_stmt|;
DECL|method|getPermalink ()
specifier|public
name|String
name|getPermalink
parameter_list|()
block|{
return|return
name|permalink
return|;
block|}
DECL|method|setPermalink (String permalink)
specifier|public
name|void
name|setPermalink
parameter_list|(
name|String
name|permalink
parameter_list|)
block|{
name|this
operator|.
name|permalink
operator|=
name|permalink
expr_stmt|;
block|}
DECL|method|getFullName ()
specifier|public
name|String
name|getFullName
parameter_list|()
block|{
return|return
name|fullName
return|;
block|}
DECL|method|setFullName (String fullName)
specifier|public
name|void
name|setFullName
parameter_list|(
name|String
name|fullName
parameter_list|)
block|{
name|this
operator|.
name|fullName
operator|=
name|fullName
expr_stmt|;
block|}
DECL|method|getUserId ()
specifier|public
name|Long
name|getUserId
parameter_list|()
block|{
return|return
name|userId
return|;
block|}
DECL|method|setUserId (Long userId)
specifier|public
name|void
name|setUserId
parameter_list|(
name|Long
name|userId
parameter_list|)
block|{
name|this
operator|.
name|userId
operator|=
name|userId
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
literal|"Name [permalink="
operator|+
name|permalink
operator|+
literal|", fullName="
operator|+
name|fullName
operator|+
literal|", userId="
operator|+
name|userId
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

