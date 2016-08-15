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
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileNotFoundException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
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
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|TransformerFactory
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
name|URIResolver
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
name|Consumer
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
name|Processor
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
name|Producer
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
name|SchematronConfigException
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
name|ClassPathURIResolver
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
name|TemplatesFactory
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
name|impl
operator|.
name|DefaultEndpoint
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
name|UriEndpoint
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
name|UriPath
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
name|util
operator|.
name|ResourceHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|io
operator|.
name|FileUtils
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

begin_import
import|import static
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
operator|.
name|LINE_NUMBERING
import|;
end_import

begin_import
import|import static
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
operator|.
name|SAXON_TRANSFORMER_FACTORY_CLASS_NAME
import|;
end_import

begin_comment
comment|/**  *  Validates the payload of a message using the Schematron Library.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"schematron"
argument_list|,
name|title
operator|=
literal|"Schematron"
argument_list|,
name|syntax
operator|=
literal|"schematron:path"
argument_list|,
name|producerOnly
operator|=
literal|true
argument_list|,
name|label
operator|=
literal|"validation"
argument_list|)
DECL|class|SchematronEndpoint
specifier|public
class|class
name|SchematronEndpoint
extends|extends
name|DefaultEndpoint
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
name|SchematronEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|transformerFactory
specifier|private
name|TransformerFactory
name|transformerFactory
decl_stmt|;
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|path
specifier|private
name|String
name|path
decl_stmt|;
annotation|@
name|UriParam
DECL|field|abort
specifier|private
name|boolean
name|abort
decl_stmt|;
annotation|@
name|UriParam
DECL|field|rules
specifier|private
name|Templates
name|rules
decl_stmt|;
annotation|@
name|UriParam
DECL|field|uriResolver
specifier|private
name|URIResolver
name|uriResolver
decl_stmt|;
DECL|method|SchematronEndpoint ()
specifier|public
name|SchematronEndpoint
parameter_list|()
block|{     }
DECL|method|SchematronEndpoint (String uri, String path, SchematronComponent component)
specifier|public
name|SchematronEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|path
parameter_list|,
name|SchematronComponent
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|path
operator|=
name|path
expr_stmt|;
block|}
DECL|method|SchematronEndpoint (String endpointUri)
specifier|public
name|SchematronEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|)
expr_stmt|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|SchematronProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Consumer is not implemented for this component"
argument_list|)
throw|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|getPath ()
specifier|public
name|String
name|getPath
parameter_list|()
block|{
return|return
name|path
return|;
block|}
comment|/**      * The path to the schematron rules file. Can either be in class path or location in the file system.      */
DECL|method|setPath (String path)
specifier|public
name|void
name|setPath
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|this
operator|.
name|path
operator|=
name|path
expr_stmt|;
block|}
comment|/**      * Flag to abort the route and throw a schematron validation exception.      */
DECL|method|setAbort (boolean abort)
specifier|public
name|void
name|setAbort
parameter_list|(
name|boolean
name|abort
parameter_list|)
block|{
name|this
operator|.
name|abort
operator|=
name|abort
expr_stmt|;
block|}
DECL|method|isAbort ()
specifier|public
name|boolean
name|isAbort
parameter_list|()
block|{
return|return
name|abort
return|;
block|}
DECL|method|getRules ()
specifier|public
name|Templates
name|getRules
parameter_list|()
block|{
return|return
name|rules
return|;
block|}
comment|/**      * To use the given schematron rules instead of loading from the path      */
DECL|method|setRules (Templates rules)
specifier|public
name|void
name|setRules
parameter_list|(
name|Templates
name|rules
parameter_list|)
block|{
name|this
operator|.
name|rules
operator|=
name|rules
expr_stmt|;
block|}
comment|/**      * Set the {@link URIResolver} to be used for resolving schematron includes in the rules file.      */
DECL|method|setUriResolver (URIResolver uriResolver)
specifier|public
name|void
name|setUriResolver
parameter_list|(
name|URIResolver
name|uriResolver
parameter_list|)
block|{
name|this
operator|.
name|uriResolver
operator|=
name|uriResolver
expr_stmt|;
block|}
DECL|method|getUriResolver ()
specifier|public
name|URIResolver
name|getUriResolver
parameter_list|()
block|{
return|return
name|uriResolver
return|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
if|if
condition|(
name|transformerFactory
operator|==
literal|null
condition|)
block|{
name|createTransformerFactory
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|rules
operator|==
literal|null
condition|)
block|{
try|try
block|{
comment|// Attempt to read the schematron rules from the class path first.
name|LOG
operator|.
name|debug
argument_list|(
literal|"Reading schematron rules from class path {}"
argument_list|,
name|path
argument_list|)
expr_stmt|;
name|InputStream
name|schRules
init|=
name|ResourceHelper
operator|.
name|resolveMandatoryResourceAsInputStream
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|path
argument_list|)
decl_stmt|;
name|rules
operator|=
name|TemplatesFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|getTemplates
argument_list|(
name|schRules
argument_list|,
name|transformerFactory
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|classPathException
parameter_list|)
block|{
comment|// Attempts from the file system.
name|LOG
operator|.
name|debug
argument_list|(
literal|"Error loading schematron rules from class path, attempting file system {}"
argument_list|,
name|path
argument_list|)
expr_stmt|;
try|try
block|{
name|InputStream
name|schRules
init|=
name|FileUtils
operator|.
name|openInputStream
argument_list|(
operator|new
name|File
argument_list|(
name|path
argument_list|)
argument_list|)
decl_stmt|;
name|rules
operator|=
name|TemplatesFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|getTemplates
argument_list|(
name|schRules
argument_list|,
name|transformerFactory
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|FileNotFoundException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Schematron rules not found in the file system {}"
argument_list|,
name|path
argument_list|)
expr_stmt|;
throw|throw
name|classPathException
throw|;
comment|// Can be more meaningful, for example, xslt compilation error.
block|}
block|}
comment|// rules not found in class path nor in file system.
if|if
condition|(
name|rules
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Failed to load schematron rules {}"
argument_list|,
name|path
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|SchematronConfigException
argument_list|(
literal|"Failed to load schematron rules: "
operator|+
name|path
argument_list|)
throw|;
block|}
block|}
block|}
DECL|method|createTransformerFactory ()
specifier|private
name|void
name|createTransformerFactory
parameter_list|()
throws|throws
name|ClassNotFoundException
block|{
comment|// provide the class loader of this component to work in OSGi environments
name|Class
argument_list|<
name|TransformerFactory
argument_list|>
name|factoryClass
init|=
name|getCamelContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveMandatoryClass
argument_list|(
name|SAXON_TRANSFORMER_FACTORY_CLASS_NAME
argument_list|,
name|TransformerFactory
operator|.
name|class
argument_list|,
name|SchematronComponent
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Using TransformerFactoryClass {}"
argument_list|,
name|factoryClass
argument_list|)
expr_stmt|;
name|transformerFactory
operator|=
name|getCamelContext
argument_list|()
operator|.
name|getInjector
argument_list|()
operator|.
name|newInstance
argument_list|(
name|factoryClass
argument_list|)
expr_stmt|;
name|transformerFactory
operator|.
name|setURIResolver
argument_list|(
operator|new
name|ClassPathURIResolver
argument_list|(
name|Constants
operator|.
name|SCHEMATRON_TEMPLATES_ROOT_DIR
argument_list|,
name|this
operator|.
name|uriResolver
argument_list|)
argument_list|)
expr_stmt|;
name|transformerFactory
operator|.
name|setAttribute
argument_list|(
name|LINE_NUMBERING
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

