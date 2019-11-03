begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.nitrite
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|nitrite
package|;
end_package

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
name|java
operator|.
name|util
operator|.
name|Map
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
name|dizitart
operator|.
name|no2
operator|.
name|Cursor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|dizitart
operator|.
name|no2
operator|.
name|Document
import|;
end_import

begin_class
annotation|@
name|Converter
argument_list|(
name|generateLoader
operator|=
literal|true
argument_list|)
DECL|class|NitriteTypeConverters
specifier|public
specifier|final
class|class
name|NitriteTypeConverters
block|{
DECL|method|NitriteTypeConverters ()
specifier|private
name|NitriteTypeConverters
parameter_list|()
block|{     }
annotation|@
name|Converter
DECL|method|fromMapToDocument (Map<String, Object> map)
specifier|public
specifier|static
name|Document
name|fromMapToDocument
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
parameter_list|)
block|{
return|return
operator|new
name|Document
argument_list|(
name|map
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|fromCursorToList (Cursor cursor)
specifier|public
specifier|static
name|List
name|fromCursorToList
parameter_list|(
name|Cursor
name|cursor
parameter_list|)
block|{
return|return
name|cursor
operator|.
name|toList
argument_list|()
return|;
block|}
block|}
end_class

end_unit

