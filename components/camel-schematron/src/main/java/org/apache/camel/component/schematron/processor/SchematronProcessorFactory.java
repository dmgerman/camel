begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.schematron.processor
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
name|processor
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|XMLConstants
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|ParserConfigurationException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|SAXParser
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|SAXParserFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Templates
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|SAXException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|XMLReader
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
name|SchematronConfigException
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
comment|/**  * Schematron Engine Factory  *  */
end_comment

begin_class
DECL|class|SchematronProcessorFactory
specifier|public
specifier|final
class|class
name|SchematronProcessorFactory
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|SchematronProcessorFactory
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * Private constructor.      */
DECL|method|SchematronProcessorFactory ()
specifier|private
name|SchematronProcessorFactory
parameter_list|()
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|()
throw|;
block|}
comment|/**      * Creates an instance of SchematronEngine      *      * @param rules the given schematron rules      * @return an instance of SchematronEngine      */
DECL|method|newSchematronEngine (final Templates rules)
specifier|public
specifier|static
name|SchematronProcessor
name|newSchematronEngine
parameter_list|(
specifier|final
name|Templates
name|rules
parameter_list|)
block|{
try|try
block|{
return|return
operator|new
name|SchematronProcessor
argument_list|(
name|getXMLReader
argument_list|()
argument_list|,
name|rules
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Failed to parse the configuration file"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|SchematronConfigException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Gets XMLReader.      *      * @return instance of XMLReader      * @throws ParserConfigurationException      * @throws SAXException      */
DECL|method|getXMLReader ()
specifier|private
specifier|static
name|XMLReader
name|getXMLReader
parameter_list|()
throws|throws
name|ParserConfigurationException
throws|,
name|SAXException
block|{
specifier|final
name|SAXParserFactory
name|fac
init|=
name|SAXParserFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|fac
operator|.
name|setFeature
argument_list|(
name|XMLConstants
operator|.
name|FEATURE_SECURE_PROCESSING
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|fac
operator|.
name|setValidating
argument_list|(
literal|false
argument_list|)
expr_stmt|;
specifier|final
name|SAXParser
name|parser
init|=
name|fac
operator|.
name|newSAXParser
argument_list|()
decl_stmt|;
name|XMLReader
name|reader
init|=
name|parser
operator|.
name|getXMLReader
argument_list|()
decl_stmt|;
return|return
name|reader
return|;
block|}
block|}
end_class

end_unit

