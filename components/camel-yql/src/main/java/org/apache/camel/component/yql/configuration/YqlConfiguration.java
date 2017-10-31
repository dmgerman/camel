begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.yql.configuration
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|yql
operator|.
name|configuration
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
name|Metadata
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
name|spi
operator|.
name|UriParam
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
name|spi
operator|.
name|UriParams
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
name|spi
operator|.
name|UriPath
import|;
end_import

begin_class
annotation|@
name|UriParams
DECL|class|YqlConfiguration
specifier|public
class|class
name|YqlConfiguration
block|{
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|query
specifier|private
name|String
name|query
decl_stmt|;
annotation|@
name|UriParam
DECL|field|format
specifier|private
name|String
name|format
init|=
literal|"json"
decl_stmt|;
annotation|@
name|UriParam
DECL|field|diagnostics
specifier|private
name|boolean
name|diagnostics
init|=
literal|false
decl_stmt|;
annotation|@
name|UriParam
DECL|field|callback
specifier|private
name|String
name|callback
init|=
literal|""
decl_stmt|;
DECL|method|getQuery ()
specifier|public
name|String
name|getQuery
parameter_list|()
block|{
return|return
name|query
return|;
block|}
comment|/**      * Set the YQL query      */
DECL|method|setQuery (final String query)
specifier|public
name|void
name|setQuery
parameter_list|(
specifier|final
name|String
name|query
parameter_list|)
block|{
name|this
operator|.
name|query
operator|=
name|query
expr_stmt|;
block|}
DECL|method|getFormat ()
specifier|public
name|String
name|getFormat
parameter_list|()
block|{
return|return
name|format
return|;
block|}
comment|/**      * Set the YQL format, xml or json      */
DECL|method|setFormat (final String format)
specifier|public
name|void
name|setFormat
parameter_list|(
specifier|final
name|String
name|format
parameter_list|)
block|{
name|this
operator|.
name|format
operator|=
name|format
expr_stmt|;
block|}
DECL|method|isDiagnostics ()
specifier|public
name|boolean
name|isDiagnostics
parameter_list|()
block|{
return|return
name|diagnostics
return|;
block|}
comment|/**      * Set if diagnostics should be included in the query      */
DECL|method|setDiagnostics (final boolean diagnostics)
specifier|public
name|void
name|setDiagnostics
parameter_list|(
specifier|final
name|boolean
name|diagnostics
parameter_list|)
block|{
name|this
operator|.
name|diagnostics
operator|=
name|diagnostics
expr_stmt|;
block|}
DECL|method|getCallback ()
specifier|public
name|String
name|getCallback
parameter_list|()
block|{
return|return
name|callback
return|;
block|}
comment|/**      * Set the callback function      */
DECL|method|setCallback (final String callback)
specifier|public
name|void
name|setCallback
parameter_list|(
specifier|final
name|String
name|callback
parameter_list|)
block|{
name|this
operator|.
name|callback
operator|=
name|callback
expr_stmt|;
block|}
block|}
end_class

end_unit

