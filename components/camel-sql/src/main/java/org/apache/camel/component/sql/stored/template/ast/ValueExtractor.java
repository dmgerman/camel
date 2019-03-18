begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sql.stored.template.ast
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sql
operator|.
name|stored
operator|.
name|template
operator|.
name|ast
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
name|Exchange
import|;
end_import

begin_comment
comment|/**  * ValueExtractotr extracts value from Exchange and Container object.  * Usually each input parameter has one extractor which extracts  * parameter value from input "map" to be sent into db.  */
end_comment

begin_interface
DECL|interface|ValueExtractor
specifier|public
interface|interface
name|ValueExtractor
block|{
DECL|method|eval (Exchange exchange, Object container)
name|Object
name|eval
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|container
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

