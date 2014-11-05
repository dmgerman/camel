begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jdbc
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jdbc
package|;
end_package

begin_comment
comment|/**  * The default {@link BeanRowMapper} will map row names to lower case names,  * but use a single upper case letter after underscores or dashes (which is skipped).  *<p/>  * For example<tt>CUST_ID</tt> is mapped as<tt>custId</tt>.  */
end_comment

begin_class
DECL|class|DefaultBeanRowMapper
specifier|public
class|class
name|DefaultBeanRowMapper
implements|implements
name|BeanRowMapper
block|{
annotation|@
name|Override
DECL|method|map (String row, Object value)
specifier|public
name|String
name|map
parameter_list|(
name|String
name|row
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
comment|// convert to lover case, and underscore as new upper case name;
return|return
name|mapRowName
argument_list|(
name|row
argument_list|)
return|;
block|}
DECL|method|mapRowName (String row)
specifier|protected
name|String
name|mapRowName
parameter_list|(
name|String
name|row
parameter_list|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|boolean
name|toUpper
init|=
literal|false
decl_stmt|;
for|for
control|(
name|char
name|ch
range|:
name|row
operator|.
name|toCharArray
argument_list|()
control|)
block|{
if|if
condition|(
name|ch
operator|==
literal|'_'
operator|||
name|ch
operator|==
literal|'-'
condition|)
block|{
name|toUpper
operator|=
literal|true
expr_stmt|;
continue|continue;
block|}
if|if
condition|(
name|toUpper
condition|)
block|{
name|char
name|upper
init|=
name|Character
operator|.
name|toUpperCase
argument_list|(
name|ch
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|upper
argument_list|)
expr_stmt|;
comment|// reset flag
name|toUpper
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
name|char
name|lower
init|=
name|Character
operator|.
name|toLowerCase
argument_list|(
name|ch
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|lower
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

