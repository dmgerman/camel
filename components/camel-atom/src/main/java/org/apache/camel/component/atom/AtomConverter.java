begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.atom
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|atom
package|;
end_package

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|DateFormat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|ParseException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|SimpleDateFormat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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
comment|/**  * Date converters.  */
end_comment

begin_class
annotation|@
name|Converter
argument_list|(
name|loader
operator|=
literal|true
argument_list|)
DECL|class|AtomConverter
specifier|public
specifier|final
class|class
name|AtomConverter
block|{
DECL|field|DATE_PATTERN_NO_TIMEZONE
specifier|public
specifier|static
specifier|final
name|String
name|DATE_PATTERN_NO_TIMEZONE
init|=
literal|"yyyy-MM-dd'T'HH:mm:ss"
decl_stmt|;
DECL|method|AtomConverter ()
specifier|private
name|AtomConverter
parameter_list|()
block|{
comment|//Helper class
block|}
annotation|@
name|Converter
DECL|method|toDate (String text)
specifier|public
specifier|static
name|Date
name|toDate
parameter_list|(
name|String
name|text
parameter_list|)
throws|throws
name|ParseException
block|{
name|DateFormat
name|sdf
init|=
operator|new
name|SimpleDateFormat
argument_list|(
name|DATE_PATTERN_NO_TIMEZONE
argument_list|)
decl_stmt|;
return|return
name|sdf
operator|.
name|parse
argument_list|(
name|text
argument_list|)
return|;
block|}
block|}
end_class

end_unit

