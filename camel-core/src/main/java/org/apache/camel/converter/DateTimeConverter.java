begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.converter
package|package
name|org
operator|.
name|apache
operator|.
name|camel
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
name|TimeZone
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

begin_comment
comment|/**  * Date and time related converters.  */
end_comment

begin_class
annotation|@
name|Converter
DECL|class|DateTimeConverter
specifier|public
specifier|final
class|class
name|DateTimeConverter
block|{
comment|/**      * Utility classes should not have a public constructor.      */
DECL|method|DateTimeConverter ()
specifier|private
name|DateTimeConverter
parameter_list|()
block|{     }
annotation|@
name|Converter
DECL|method|toTimeZone (String s)
specifier|public
specifier|static
name|TimeZone
name|toTimeZone
parameter_list|(
name|String
name|s
parameter_list|)
block|{
return|return
name|TimeZone
operator|.
name|getTimeZone
argument_list|(
name|s
argument_list|)
return|;
block|}
block|}
end_class

end_unit

