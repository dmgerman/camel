begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.schematron.constant
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|schematron
operator|.
name|constant
package|;
end_package

begin_comment
comment|/**  * Utility class defining all constants needed for the schematron component.  *<p/>  */
end_comment

begin_class
DECL|class|Constants
specifier|public
specifier|final
class|class
name|Constants
block|{
DECL|field|VALIDATION_STATUS
specifier|public
specifier|static
specifier|final
name|String
name|VALIDATION_STATUS
init|=
literal|"CamelSchematronValidationStatus"
decl_stmt|;
DECL|field|VALIDATION_REPORT
specifier|public
specifier|static
specifier|final
name|String
name|VALIDATION_REPORT
init|=
literal|"CamelSchematronValidationReport"
decl_stmt|;
DECL|field|HTTP_PURL_OCLC_ORG_DSDL_SVRL
specifier|public
specifier|static
specifier|final
name|String
name|HTTP_PURL_OCLC_ORG_DSDL_SVRL
init|=
literal|"http://purl.oclc.org/dsdl/svrl"
decl_stmt|;
DECL|field|FAILED_ASSERT
specifier|public
specifier|static
specifier|final
name|String
name|FAILED_ASSERT
init|=
literal|"failed-assert"
decl_stmt|;
DECL|field|FAILED
specifier|public
specifier|static
specifier|final
name|String
name|FAILED
init|=
literal|"FAILED"
decl_stmt|;
DECL|field|SUCCESS
specifier|public
specifier|static
specifier|final
name|String
name|SUCCESS
init|=
literal|"SUCCESS"
decl_stmt|;
DECL|field|SCHEMATRON_TEMPLATES_ROOT_DIR
specifier|public
specifier|static
specifier|final
name|String
name|SCHEMATRON_TEMPLATES_ROOT_DIR
init|=
literal|"iso-schematron-xslt2"
decl_stmt|;
DECL|method|Constants ()
specifier|private
name|Constants
parameter_list|()
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Utility class should not be instantiated"
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

