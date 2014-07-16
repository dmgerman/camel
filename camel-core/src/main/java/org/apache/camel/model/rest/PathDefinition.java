begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model.rest
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|rest
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAttribute
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlElementRef
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlTransient
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
name|model
operator|.
name|ToDefinition
import|;
end_import

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"path"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|PROPERTY
argument_list|)
DECL|class|PathDefinition
specifier|public
class|class
name|PathDefinition
block|{
DECL|field|uri
specifier|private
name|String
name|uri
decl_stmt|;
DECL|field|verbs
specifier|private
name|List
argument_list|<
name|VerbDefinition
argument_list|>
name|verbs
init|=
operator|new
name|ArrayList
argument_list|<
name|VerbDefinition
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|rest
specifier|private
name|RestDefinition
name|rest
decl_stmt|;
DECL|method|getUri ()
specifier|public
name|String
name|getUri
parameter_list|()
block|{
return|return
name|uri
return|;
block|}
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|method|setUri (String uri)
specifier|public
name|void
name|setUri
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|this
operator|.
name|uri
operator|=
name|uri
expr_stmt|;
block|}
DECL|method|getVerbs ()
specifier|public
name|List
argument_list|<
name|VerbDefinition
argument_list|>
name|getVerbs
parameter_list|()
block|{
return|return
name|verbs
return|;
block|}
annotation|@
name|XmlElementRef
DECL|method|setVerbs (List<VerbDefinition> verbs)
specifier|public
name|void
name|setVerbs
parameter_list|(
name|List
argument_list|<
name|VerbDefinition
argument_list|>
name|verbs
parameter_list|)
block|{
name|this
operator|.
name|verbs
operator|=
name|verbs
expr_stmt|;
block|}
DECL|method|getRest ()
specifier|public
name|RestDefinition
name|getRest
parameter_list|()
block|{
return|return
name|rest
return|;
block|}
annotation|@
name|XmlTransient
DECL|method|setRest (RestDefinition rest)
specifier|public
name|void
name|setRest
parameter_list|(
name|RestDefinition
name|rest
parameter_list|)
block|{
name|this
operator|.
name|rest
operator|=
name|rest
expr_stmt|;
block|}
comment|// Fluent API
comment|//-------------------------------------------------------------------------
DECL|method|path (String uri)
specifier|public
name|PathDefinition
name|path
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
comment|// add a new path on the rest
return|return
name|rest
operator|.
name|path
argument_list|(
name|uri
argument_list|)
return|;
block|}
DECL|method|get ()
specifier|public
name|PathDefinition
name|get
parameter_list|()
block|{
return|return
name|addVerb
argument_list|(
literal|"get"
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|post ()
specifier|public
name|PathDefinition
name|post
parameter_list|()
block|{
return|return
name|addVerb
argument_list|(
literal|"post"
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|put ()
specifier|public
name|PathDefinition
name|put
parameter_list|()
block|{
return|return
name|addVerb
argument_list|(
literal|"put"
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|delete ()
specifier|public
name|PathDefinition
name|delete
parameter_list|()
block|{
return|return
name|addVerb
argument_list|(
literal|"delete"
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|head ()
specifier|public
name|PathDefinition
name|head
parameter_list|()
block|{
return|return
name|addVerb
argument_list|(
literal|"head"
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|verb (String verb)
specifier|public
name|PathDefinition
name|verb
parameter_list|(
name|String
name|verb
parameter_list|)
block|{
return|return
name|addVerb
argument_list|(
name|verb
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|accept (String accept)
specifier|public
name|PathDefinition
name|accept
parameter_list|(
name|String
name|accept
parameter_list|)
block|{
comment|// add to last verb
if|if
condition|(
name|getVerbs
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Must add verb first, such as get/post/delete"
argument_list|)
throw|;
block|}
name|VerbDefinition
name|verb
init|=
name|getVerbs
argument_list|()
operator|.
name|get
argument_list|(
name|getVerbs
argument_list|()
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
name|verb
operator|.
name|setAccept
argument_list|(
name|accept
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Deprecated
DECL|method|to (String url)
specifier|public
name|VerbDefinition
name|to
parameter_list|(
name|String
name|url
parameter_list|)
block|{
comment|// add to last verb
if|if
condition|(
name|getVerbs
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Must add verb first, such as get/post/delete"
argument_list|)
throw|;
block|}
name|VerbDefinition
name|verb
init|=
name|getVerbs
argument_list|()
operator|.
name|get
argument_list|(
name|getVerbs
argument_list|()
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
name|verb
operator|.
name|addOutput
argument_list|(
operator|new
name|ToDefinition
argument_list|(
name|url
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|verb
return|;
block|}
DECL|method|addVerb (String verb, String url)
specifier|private
name|PathDefinition
name|addVerb
parameter_list|(
name|String
name|verb
parameter_list|,
name|String
name|url
parameter_list|)
block|{
name|VerbDefinition
name|answer
init|=
operator|new
name|VerbDefinition
argument_list|()
decl_stmt|;
name|answer
operator|.
name|setMethod
argument_list|(
name|verb
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setPath
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|getVerbs
argument_list|()
operator|.
name|add
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
end_class

end_unit

