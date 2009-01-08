begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.osgi.converter
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|osgi
operator|.
name|converter
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Converter
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
name|ObjectHelper
import|;
end_import

begin_class
DECL|class|MyTypeConverter
specifier|public
specifier|final
class|class
name|MyTypeConverter
block|{
comment|/**      * Utility classes should not have a public constructor.      */
DECL|method|MyTypeConverter ()
specifier|private
name|MyTypeConverter
parameter_list|()
block|{     }
comment|/**      * Converts the given value to a boolean, handling strings or Boolean      * objects; otherwise returning false if the value could not be converted to      * a boolean      */
annotation|@
name|Converter
DECL|method|toBool (Object value)
specifier|public
specifier|static
name|boolean
name|toBool
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
name|Boolean
name|answer
init|=
name|toBoolean
argument_list|(
name|value
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|!=
literal|null
condition|)
block|{
return|return
name|answer
operator|.
name|booleanValue
argument_list|()
return|;
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Converts the given value to a Boolean, handling strings or Boolean      * objects; otherwise returning null if the value cannot be converted to a      * boolean      */
annotation|@
name|Converter
DECL|method|toBoolean (Object value)
specifier|public
specifier|static
name|Boolean
name|toBoolean
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
return|return
name|ObjectHelper
operator|.
name|toBoolean
argument_list|(
name|value
argument_list|)
return|;
block|}
block|}
end_class

end_unit

