begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlEnum
import|;
end_import

begin_comment
comment|/**  * Level of granularity for performance statistics enabled  */
end_comment

begin_enum
annotation|@
name|XmlEnum
DECL|enum|ManagementStatisticsLevel
specifier|public
enum|enum
name|ManagementStatisticsLevel
block|{
DECL|enumConstant|Extended
DECL|enumConstant|Default
DECL|enumConstant|RoutesOnly
DECL|enumConstant|Off
name|Extended
block|,
name|Default
block|,
name|RoutesOnly
block|,
name|Off
block|;
DECL|method|isDefaultOrExtended ()
specifier|public
name|boolean
name|isDefaultOrExtended
parameter_list|()
block|{
return|return
name|ordinal
argument_list|()
operator|==
name|Default
operator|.
name|ordinal
argument_list|()
operator|||
name|ordinal
argument_list|()
operator|==
name|Extended
operator|.
name|ordinal
argument_list|()
return|;
block|}
DECL|method|isExtended ()
specifier|public
name|boolean
name|isExtended
parameter_list|()
block|{
return|return
name|ordinal
argument_list|()
operator|==
name|Extended
operator|.
name|ordinal
argument_list|()
return|;
block|}
block|}
end_enum

end_unit
