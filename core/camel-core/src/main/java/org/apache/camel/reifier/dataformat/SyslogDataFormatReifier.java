begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.reifier.dataformat
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|reifier
operator|.
name|dataformat
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|DataFormatDefinition
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
name|model
operator|.
name|dataformat
operator|.
name|SyslogDataFormat
import|;
end_import

begin_class
DECL|class|SyslogDataFormatReifier
specifier|public
class|class
name|SyslogDataFormatReifier
extends|extends
name|DataFormatReifier
argument_list|<
name|SyslogDataFormat
argument_list|>
block|{
DECL|method|SyslogDataFormatReifier (DataFormatDefinition definition)
specifier|public
name|SyslogDataFormatReifier
parameter_list|(
name|DataFormatDefinition
name|definition
parameter_list|)
block|{
name|super
argument_list|(
operator|(
name|SyslogDataFormat
operator|)
name|definition
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

