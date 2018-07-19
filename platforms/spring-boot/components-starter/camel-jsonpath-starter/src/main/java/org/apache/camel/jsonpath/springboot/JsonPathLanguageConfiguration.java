begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.jsonpath.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|jsonpath
operator|.
name|springboot
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Generated
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
name|spring
operator|.
name|boot
operator|.
name|LanguageConfigurationPropertiesCommon
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationProperties
import|;
end_import

begin_comment
comment|/**  * To use JsonPath in Camel expressions or predicates.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.SpringBootAutoConfigurationMojo"
argument_list|)
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.language.jsonpath"
argument_list|)
DECL|class|JsonPathLanguageConfiguration
specifier|public
class|class
name|JsonPathLanguageConfiguration
extends|extends
name|LanguageConfigurationPropertiesCommon
block|{
comment|/**      * Whether to enable auto configuration of the jsonpath language. This is      * enabled by default.      */
DECL|field|enabled
specifier|private
name|Boolean
name|enabled
decl_stmt|;
comment|/**      * Whether to suppress exceptions such as PathNotFoundException.      */
DECL|field|suppressExceptions
specifier|private
name|Boolean
name|suppressExceptions
init|=
literal|false
decl_stmt|;
comment|/**      * Whether to allow in inlined simple exceptions in the JsonPath expression      */
DECL|field|allowSimple
specifier|private
name|Boolean
name|allowSimple
init|=
literal|true
decl_stmt|;
comment|/**      * Whether to allow using the easy predicate parser to pre-parse predicates.      */
DECL|field|allowEasyPredicate
specifier|private
name|Boolean
name|allowEasyPredicate
init|=
literal|true
decl_stmt|;
comment|/**      * Whether to write the output of each row/element as a JSON String value      * instead of a Map/POJO value.      */
DECL|field|writeAsString
specifier|private
name|Boolean
name|writeAsString
init|=
literal|false
decl_stmt|;
comment|/**      * Name of header to use as input, instead of the message body      */
DECL|field|headerName
specifier|private
name|String
name|headerName
decl_stmt|;
comment|/**      * Whether to trim the value to remove leading and trailing whitespaces and      * line breaks      */
DECL|field|trim
specifier|private
name|Boolean
name|trim
init|=
literal|true
decl_stmt|;
DECL|method|getSuppressExceptions ()
specifier|public
name|Boolean
name|getSuppressExceptions
parameter_list|()
block|{
return|return
name|suppressExceptions
return|;
block|}
DECL|method|setSuppressExceptions (Boolean suppressExceptions)
specifier|public
name|void
name|setSuppressExceptions
parameter_list|(
name|Boolean
name|suppressExceptions
parameter_list|)
block|{
name|this
operator|.
name|suppressExceptions
operator|=
name|suppressExceptions
expr_stmt|;
block|}
DECL|method|getAllowSimple ()
specifier|public
name|Boolean
name|getAllowSimple
parameter_list|()
block|{
return|return
name|allowSimple
return|;
block|}
DECL|method|setAllowSimple (Boolean allowSimple)
specifier|public
name|void
name|setAllowSimple
parameter_list|(
name|Boolean
name|allowSimple
parameter_list|)
block|{
name|this
operator|.
name|allowSimple
operator|=
name|allowSimple
expr_stmt|;
block|}
DECL|method|getAllowEasyPredicate ()
specifier|public
name|Boolean
name|getAllowEasyPredicate
parameter_list|()
block|{
return|return
name|allowEasyPredicate
return|;
block|}
DECL|method|setAllowEasyPredicate (Boolean allowEasyPredicate)
specifier|public
name|void
name|setAllowEasyPredicate
parameter_list|(
name|Boolean
name|allowEasyPredicate
parameter_list|)
block|{
name|this
operator|.
name|allowEasyPredicate
operator|=
name|allowEasyPredicate
expr_stmt|;
block|}
DECL|method|getWriteAsString ()
specifier|public
name|Boolean
name|getWriteAsString
parameter_list|()
block|{
return|return
name|writeAsString
return|;
block|}
DECL|method|setWriteAsString (Boolean writeAsString)
specifier|public
name|void
name|setWriteAsString
parameter_list|(
name|Boolean
name|writeAsString
parameter_list|)
block|{
name|this
operator|.
name|writeAsString
operator|=
name|writeAsString
expr_stmt|;
block|}
DECL|method|getHeaderName ()
specifier|public
name|String
name|getHeaderName
parameter_list|()
block|{
return|return
name|headerName
return|;
block|}
DECL|method|setHeaderName (String headerName)
specifier|public
name|void
name|setHeaderName
parameter_list|(
name|String
name|headerName
parameter_list|)
block|{
name|this
operator|.
name|headerName
operator|=
name|headerName
expr_stmt|;
block|}
DECL|method|getTrim ()
specifier|public
name|Boolean
name|getTrim
parameter_list|()
block|{
return|return
name|trim
return|;
block|}
DECL|method|setTrim (Boolean trim)
specifier|public
name|void
name|setTrim
parameter_list|(
name|Boolean
name|trim
parameter_list|)
block|{
name|this
operator|.
name|trim
operator|=
name|trim
expr_stmt|;
block|}
block|}
end_class

end_unit

