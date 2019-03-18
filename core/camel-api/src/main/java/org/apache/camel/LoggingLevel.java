begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  * Used to configure the logging levels  */
end_comment

begin_enum
annotation|@
name|XmlEnum
DECL|enum|LoggingLevel
specifier|public
enum|enum
name|LoggingLevel
block|{
DECL|enumConstant|TRACE
DECL|enumConstant|DEBUG
DECL|enumConstant|INFO
DECL|enumConstant|WARN
DECL|enumConstant|ERROR
DECL|enumConstant|OFF
name|TRACE
block|,
name|DEBUG
block|,
name|INFO
block|,
name|WARN
block|,
name|ERROR
block|,
name|OFF
block|;
comment|/**      * Is the given logging level equal or higher than the current level.      */
DECL|method|isEnabled (LoggingLevel level)
specifier|public
name|boolean
name|isEnabled
parameter_list|(
name|LoggingLevel
name|level
parameter_list|)
block|{
comment|// off is always false
if|if
condition|(
name|this
operator|==
name|OFF
operator|||
name|level
operator|==
name|OFF
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|this
operator|.
name|compareTo
argument_list|(
name|level
argument_list|)
operator|<=
literal|0
return|;
block|}
block|}
end_enum

end_unit

