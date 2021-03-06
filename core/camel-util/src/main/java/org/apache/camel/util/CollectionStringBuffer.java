begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_comment
comment|/**  * A little helper class for converting a collection of values to a (usually comma separated) string.  */
end_comment

begin_class
DECL|class|CollectionStringBuffer
specifier|public
class|class
name|CollectionStringBuffer
block|{
DECL|field|buffer
specifier|private
specifier|final
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
DECL|field|separator
specifier|private
name|String
name|separator
decl_stmt|;
DECL|field|first
specifier|private
name|boolean
name|first
init|=
literal|true
decl_stmt|;
DECL|method|CollectionStringBuffer ()
specifier|public
name|CollectionStringBuffer
parameter_list|()
block|{
name|this
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
block|}
DECL|method|CollectionStringBuffer (String separator)
specifier|public
name|CollectionStringBuffer
parameter_list|(
name|String
name|separator
parameter_list|)
block|{
name|this
operator|.
name|separator
operator|=
name|separator
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
name|buffer
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|append (Object value)
specifier|public
name|void
name|append
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|first
condition|)
block|{
name|first
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
name|buffer
operator|.
name|append
argument_list|(
name|separator
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
DECL|method|getSeparator ()
specifier|public
name|String
name|getSeparator
parameter_list|()
block|{
return|return
name|separator
return|;
block|}
DECL|method|setSeparator (String separator)
specifier|public
name|void
name|setSeparator
parameter_list|(
name|String
name|separator
parameter_list|)
block|{
name|this
operator|.
name|separator
operator|=
name|separator
expr_stmt|;
block|}
DECL|method|isEmpty ()
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|first
return|;
block|}
block|}
end_class

end_unit

