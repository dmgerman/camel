begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
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
comment|/**  * Used to configure the logging levels  *  * @version $Revision: 703680 $  */
end_comment

begin_enum
annotation|@
name|XmlEnum
DECL|enum|LoggingLevel
specifier|public
enum|enum
name|LoggingLevel
block|{
DECL|enumConstant|DEBUG
DECL|enumConstant|ERROR
DECL|enumConstant|FATAL
DECL|enumConstant|INFO
DECL|enumConstant|TRACE
DECL|enumConstant|WARN
DECL|enumConstant|OFF
name|DEBUG
block|,
name|ERROR
block|,
name|FATAL
block|,
name|INFO
block|,
name|TRACE
block|,
name|WARN
block|,
name|OFF
block|}
end_enum

end_unit

