begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty4.http
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|netty4
operator|.
name|http
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashSet
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|EndpointHelper
import|;
end_import

begin_comment
comment|/**  * A default {@link SecurityConstraint} which can be used to define a set of mappings to  * as constraints.  *<p/>  * This constraint will match as<tt>true</tt> if no inclusions has been defined.  * First all the inclusions is check for matching. If a inclusion matches,  * then the exclusion is checked, and if any of them matches, then the exclusion  * will override the match and force returning<tt>false</tt>.  *<p/>  * Wildcards and regular expressions is supported as this implementation uses  * {@link EndpointHelper#matchPattern(String, String)} method for matching.  *<p/>  * This restricted constraint allows you to setup context path rules that will restrict  * access to paths, and then override and have exclusions that may allow access to  * public paths.  */
end_comment

begin_class
DECL|class|SecurityConstraintMapping
specifier|public
class|class
name|SecurityConstraintMapping
implements|implements
name|SecurityConstraint
block|{
comment|// url -> roles
DECL|field|inclusions
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|inclusions
decl_stmt|;
comment|// url
DECL|field|exclusions
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|exclusions
decl_stmt|;
annotation|@
name|Override
DECL|method|restricted (String url)
specifier|public
name|String
name|restricted
parameter_list|(
name|String
name|url
parameter_list|)
block|{
comment|// check excluded first
if|if
condition|(
name|excludedUrl
argument_list|(
name|url
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// is the url restricted?
name|String
name|constraint
init|=
name|includedUrl
argument_list|(
name|url
argument_list|)
decl_stmt|;
if|if
condition|(
name|constraint
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// is there any roles for the restricted url?
name|String
name|roles
init|=
name|inclusions
operator|!=
literal|null
condition|?
name|inclusions
operator|.
name|get
argument_list|(
name|constraint
argument_list|)
else|:
literal|null
decl_stmt|;
if|if
condition|(
name|roles
operator|==
literal|null
condition|)
block|{
comment|// use wildcard to indicate any role is accepted
return|return
literal|"*"
return|;
block|}
else|else
block|{
return|return
name|roles
return|;
block|}
block|}
DECL|method|includedUrl (String url)
specifier|private
name|String
name|includedUrl
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|String
name|candidate
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|inclusions
operator|!=
literal|null
operator|&&
operator|!
name|inclusions
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|String
name|constraint
range|:
name|inclusions
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
name|EndpointHelper
operator|.
name|matchPattern
argument_list|(
name|url
argument_list|,
name|constraint
argument_list|)
condition|)
block|{
if|if
condition|(
name|candidate
operator|==
literal|null
condition|)
block|{
name|candidate
operator|=
name|constraint
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|constraint
operator|.
name|length
argument_list|()
operator|>
name|candidate
operator|.
name|length
argument_list|()
condition|)
block|{
comment|// we want the constraint that has the longest context-path matching as its
comment|// the most explicit for the target url
name|candidate
operator|=
name|constraint
expr_stmt|;
block|}
block|}
block|}
return|return
name|candidate
return|;
block|}
comment|// by default if no included has been configured then everything is restricted
return|return
literal|"*"
return|;
block|}
DECL|method|excludedUrl (String url)
specifier|private
name|boolean
name|excludedUrl
parameter_list|(
name|String
name|url
parameter_list|)
block|{
if|if
condition|(
name|exclusions
operator|!=
literal|null
operator|&&
operator|!
name|exclusions
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|String
name|constraint
range|:
name|exclusions
control|)
block|{
if|if
condition|(
name|EndpointHelper
operator|.
name|matchPattern
argument_list|(
name|url
argument_list|,
name|constraint
argument_list|)
condition|)
block|{
comment|// force not matches if this was an exclusion
return|return
literal|true
return|;
block|}
block|}
block|}
return|return
literal|false
return|;
block|}
DECL|method|addInclusion (String constraint)
specifier|public
name|void
name|addInclusion
parameter_list|(
name|String
name|constraint
parameter_list|)
block|{
if|if
condition|(
name|inclusions
operator|==
literal|null
condition|)
block|{
name|inclusions
operator|=
operator|new
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|inclusions
operator|.
name|put
argument_list|(
name|constraint
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|addInclusion (String constraint, String roles)
specifier|public
name|void
name|addInclusion
parameter_list|(
name|String
name|constraint
parameter_list|,
name|String
name|roles
parameter_list|)
block|{
if|if
condition|(
name|inclusions
operator|==
literal|null
condition|)
block|{
name|inclusions
operator|=
operator|new
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|inclusions
operator|.
name|put
argument_list|(
name|constraint
argument_list|,
name|roles
argument_list|)
expr_stmt|;
block|}
DECL|method|addExclusion (String constraint)
specifier|public
name|void
name|addExclusion
parameter_list|(
name|String
name|constraint
parameter_list|)
block|{
if|if
condition|(
name|exclusions
operator|==
literal|null
condition|)
block|{
name|exclusions
operator|=
operator|new
name|LinkedHashSet
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|exclusions
operator|.
name|add
argument_list|(
name|constraint
argument_list|)
expr_stmt|;
block|}
DECL|method|setInclusions (Map<String, String> inclusions)
specifier|public
name|void
name|setInclusions
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|inclusions
parameter_list|)
block|{
name|this
operator|.
name|inclusions
operator|=
name|inclusions
expr_stmt|;
block|}
DECL|method|setExclusions (Set<String> exclusions)
specifier|public
name|void
name|setExclusions
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|exclusions
parameter_list|)
block|{
name|this
operator|.
name|exclusions
operator|=
name|exclusions
expr_stmt|;
block|}
block|}
end_class

end_unit

