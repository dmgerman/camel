begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.schematron
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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|Exchange
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
name|component
operator|.
name|schematron
operator|.
name|constant
operator|.
name|Constants
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
name|component
operator|.
name|schematron
operator|.
name|exception
operator|.
name|SchematronValidationException
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
name|component
operator|.
name|schematron
operator|.
name|processor
operator|.
name|SchematronProcessorFactory
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
name|support
operator|.
name|DefaultProducer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * The Schematron producer.  */
end_comment

begin_class
DECL|class|SchematronProducer
specifier|public
class|class
name|SchematronProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|logger
specifier|private
name|Logger
name|logger
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|SchematronProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|endpoint
specifier|private
name|SchematronEndpoint
name|endpoint
decl_stmt|;
comment|/**      * @param endpoint the schematron endpoint.      */
DECL|method|SchematronProducer (final SchematronEndpoint endpoint)
specifier|public
name|SchematronProducer
parameter_list|(
specifier|final
name|SchematronEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
block|}
comment|/**      * Processes the payload. Validates the XML using the SchematronEngine      *      * @param exchange      * @throws Exception      */
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|payload
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|logger
operator|.
name|debug
argument_list|(
literal|"Applying schematron validation on payload: {}"
argument_list|,
name|payload
argument_list|)
expr_stmt|;
name|String
name|report
init|=
name|SchematronProcessorFactory
operator|.
name|newSchematronEngine
argument_list|(
name|endpoint
operator|.
name|getRules
argument_list|()
argument_list|)
operator|.
name|validate
argument_list|(
name|payload
argument_list|)
decl_stmt|;
name|logger
operator|.
name|debug
argument_list|(
literal|"Schematron validation report \n {}"
argument_list|,
name|report
argument_list|)
expr_stmt|;
name|String
name|status
init|=
name|getValidationStatus
argument_list|(
name|report
argument_list|)
decl_stmt|;
name|logger
operator|.
name|info
argument_list|(
literal|"Schematron validation status : {}"
argument_list|,
name|status
argument_list|)
expr_stmt|;
name|setValidationReport
argument_list|(
name|exchange
argument_list|,
name|report
argument_list|,
name|status
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets validation report and status      *      * @param exchange      * @param report      * @param status      */
DECL|method|setValidationReport (Exchange exchange, String report, String status)
specifier|private
name|void
name|setValidationReport
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|report
parameter_list|,
name|String
name|status
parameter_list|)
block|{
comment|// if exchange pattern is In and Out set details on the Out message.
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|Constants
operator|.
name|VALIDATION_STATUS
argument_list|,
name|status
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|Constants
operator|.
name|VALIDATION_REPORT
argument_list|,
name|report
argument_list|)
expr_stmt|;
if|if
condition|(
name|exchange
operator|.
name|getPattern
argument_list|()
operator|.
name|isOutCapable
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeaders
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeaders
argument_list|()
operator|.
name|putAll
argument_list|(
name|headers
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
operator|.
name|putAll
argument_list|(
name|headers
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Get validation status, SUCCESS or FAILURE      *      * @param report      * @return      */
DECL|method|getValidationStatus (final String report)
specifier|private
name|String
name|getValidationStatus
parameter_list|(
specifier|final
name|String
name|report
parameter_list|)
block|{
name|String
name|status
init|=
name|report
operator|.
name|contains
argument_list|(
name|Constants
operator|.
name|FAILED_ASSERT
argument_list|)
condition|?
name|Constants
operator|.
name|FAILED
else|:
name|Constants
operator|.
name|SUCCESS
decl_stmt|;
if|if
condition|(
name|this
operator|.
name|endpoint
operator|.
name|isAbort
argument_list|()
operator|&&
name|Constants
operator|.
name|FAILED
operator|.
name|equals
argument_list|(
name|status
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|SchematronValidationException
argument_list|(
literal|"Schematron validation failure \n"
operator|+
name|report
argument_list|)
throw|;
block|}
return|return
name|status
return|;
block|}
block|}
end_class

end_unit

