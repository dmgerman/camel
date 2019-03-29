begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|sql
operator|.
name|Timestamp
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
comment|/**  * SQL Date and time related converters.  */
end_comment

begin_class
annotation|@
name|Converter
argument_list|(
name|loader
operator|=
literal|true
argument_list|)
DECL|class|SQLConverter
specifier|public
specifier|final
class|class
name|SQLConverter
block|{
comment|/**      * Utility classes should not have a public constructor.      */
DECL|method|SQLConverter ()
specifier|private
name|SQLConverter
parameter_list|()
block|{     }
annotation|@
name|Converter
DECL|method|toTimestamp (Long l)
specifier|public
specifier|static
name|Timestamp
name|toTimestamp
parameter_list|(
name|Long
name|l
parameter_list|)
block|{
return|return
operator|new
name|Timestamp
argument_list|(
name|l
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toLong (Timestamp ts)
specifier|public
specifier|static
name|Long
name|toLong
parameter_list|(
name|Timestamp
name|ts
parameter_list|)
block|{
return|return
name|ts
operator|.
name|getTime
argument_list|()
return|;
block|}
block|}
end_class

end_unit

