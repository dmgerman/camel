begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.castor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|castor
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
name|spi
operator|.
name|DataFormat
import|;
end_import

begin_import
import|import
name|org
operator|.
name|exolab
operator|.
name|castor
operator|.
name|xml
operator|.
name|XMLContext
import|;
end_import

begin_comment
comment|/**  * A<a href="http://camel.apache.org/data-format.html">data format</a>  * ({@link DataFormat}) using Castor to marshal to and from XML  *  * @version   */
end_comment

begin_class
DECL|class|CastorDataFormat
specifier|public
class|class
name|CastorDataFormat
extends|extends
name|AbstractCastorDataFormat
block|{
DECL|method|CastorDataFormat ()
specifier|public
name|CastorDataFormat
parameter_list|()
block|{     }
DECL|method|CastorDataFormat (XMLContext xmlContext)
specifier|public
name|CastorDataFormat
parameter_list|(
name|XMLContext
name|xmlContext
parameter_list|)
block|{
name|super
argument_list|(
name|xmlContext
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

