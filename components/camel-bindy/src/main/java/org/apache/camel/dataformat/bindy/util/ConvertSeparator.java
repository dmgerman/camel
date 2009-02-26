begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.bindy.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|bindy
operator|.
name|util
package|;
end_package

begin_comment
comment|/**  *   * The ConvertSeparator helps to return the char associated to the unicode string  *  */
end_comment

begin_class
DECL|class|ConvertSeparator
specifier|public
specifier|final
class|class
name|ConvertSeparator
block|{
DECL|method|ConvertSeparator ()
specifier|private
name|ConvertSeparator
parameter_list|()
block|{
comment|// helper class
block|}
DECL|method|getCharDelimitor (String separator)
specifier|public
specifier|static
name|char
name|getCharDelimitor
parameter_list|(
name|String
name|separator
parameter_list|)
block|{
if|if
condition|(
name|separator
operator|.
name|equals
argument_list|(
literal|"\\u0001"
argument_list|)
condition|)
block|{
return|return
literal|'\u0001'
return|;
block|}
elseif|else
if|if
condition|(
name|separator
operator|.
name|equals
argument_list|(
literal|"\\t"
argument_list|)
operator|||
name|separator
operator|.
name|equals
argument_list|(
literal|"\\u0009"
argument_list|)
condition|)
block|{
return|return
literal|'\u0009'
return|;
block|}
else|else
block|{
return|return
name|separator
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

