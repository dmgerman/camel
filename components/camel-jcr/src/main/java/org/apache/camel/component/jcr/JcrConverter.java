begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jcr
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jcr
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Calendar
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jcr
operator|.
name|Value
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
name|jackrabbit
operator|.
name|value
operator|.
name|BinaryValue
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|jackrabbit
operator|.
name|value
operator|.
name|BooleanValue
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|jackrabbit
operator|.
name|value
operator|.
name|DateValue
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|jackrabbit
operator|.
name|value
operator|.
name|StringValue
import|;
end_import

begin_comment
comment|/**  * A helper class to transform Object into JCR {@link Value} implementations   */
end_comment

begin_class
annotation|@
name|Converter
DECL|class|JcrConverter
specifier|public
class|class
name|JcrConverter
block|{
comment|/**      * Converts a {@link Boolean} into a {@link Value}      * @param bool the boolean      * @return the value      */
annotation|@
name|Converter
DECL|method|toValue (Boolean bool)
specifier|public
name|Value
name|toValue
parameter_list|(
name|Boolean
name|bool
parameter_list|)
block|{
return|return
operator|new
name|BooleanValue
argument_list|(
name|bool
argument_list|)
return|;
block|}
comment|/**      * Converts an {@link InputStream} into a {@link Value}      * @param stream the input stream      * @return the value      */
annotation|@
name|Converter
DECL|method|toValue (InputStream stream)
specifier|public
name|Value
name|toValue
parameter_list|(
name|InputStream
name|stream
parameter_list|)
block|{
return|return
operator|new
name|BinaryValue
argument_list|(
name|stream
argument_list|)
return|;
block|}
comment|/**      * Converts a {@link Calendar} into a {@link Value}      * @param calendar the calendar      * @return the value      */
annotation|@
name|Converter
DECL|method|toValue (Calendar calendar)
specifier|public
name|Value
name|toValue
parameter_list|(
name|Calendar
name|calendar
parameter_list|)
block|{
return|return
operator|new
name|DateValue
argument_list|(
name|calendar
argument_list|)
return|;
block|}
comment|/**      * Converts a {@link String} into a {@link Value}      * @param value the string      * @return the value      */
annotation|@
name|Converter
DECL|method|toValue (String value)
specifier|public
name|Value
name|toValue
parameter_list|(
name|String
name|value
parameter_list|)
block|{
return|return
operator|new
name|StringValue
argument_list|(
name|value
argument_list|)
return|;
block|}
block|}
end_class

end_unit

