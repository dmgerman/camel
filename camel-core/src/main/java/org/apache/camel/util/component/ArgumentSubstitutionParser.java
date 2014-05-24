begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
DECL|package|org.apache.camel.util.component
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|component
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
import|;
end_import

begin_comment
comment|/**  * Adds support for parameter name substitutions.  */
end_comment

begin_class
DECL|class|ArgumentSubstitutionParser
specifier|public
class|class
name|ArgumentSubstitutionParser
parameter_list|<
name|T
parameter_list|>
extends|extends
name|ApiMethodParser
argument_list|<
name|T
argument_list|>
block|{
DECL|field|methodMap
specifier|private
specifier|final
name|Map
argument_list|<
name|Pattern
argument_list|,
name|Map
argument_list|<
name|Pattern
argument_list|,
name|List
argument_list|<
name|NameReplacement
argument_list|>
argument_list|>
argument_list|>
name|methodMap
decl_stmt|;
comment|/**      * Create a parser using regular expressions to adapt parameter names.      * @param proxyType Proxy class.      * @param substitutions an array of<b>ordered</b> Argument adapters.      */
DECL|method|ArgumentSubstitutionParser (Class<T> proxyType, Substitution[] substitutions)
specifier|public
name|ArgumentSubstitutionParser
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|proxyType
parameter_list|,
name|Substitution
index|[]
name|substitutions
parameter_list|)
block|{
name|super
argument_list|(
name|proxyType
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|NameReplacement
argument_list|>
argument_list|>
argument_list|>
name|regexMap
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|NameReplacement
argument_list|>
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Substitution
name|tuple
range|:
name|substitutions
control|)
block|{
name|tuple
operator|.
name|validate
argument_list|()
expr_stmt|;
specifier|final
name|NameReplacement
name|nameReplacement
init|=
operator|new
name|NameReplacement
argument_list|()
decl_stmt|;
name|nameReplacement
operator|.
name|replacement
operator|=
name|tuple
operator|.
name|replacement
expr_stmt|;
if|if
condition|(
name|tuple
operator|.
name|argType
operator|!=
literal|null
condition|)
block|{
name|nameReplacement
operator|.
name|type
operator|=
name|forName
argument_list|(
name|tuple
operator|.
name|argType
argument_list|)
expr_stmt|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|NameReplacement
argument_list|>
argument_list|>
name|replacementMap
init|=
name|regexMap
operator|.
name|get
argument_list|(
name|tuple
operator|.
name|method
argument_list|)
decl_stmt|;
if|if
condition|(
name|replacementMap
operator|==
literal|null
condition|)
block|{
name|replacementMap
operator|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|NameReplacement
argument_list|>
argument_list|>
argument_list|()
expr_stmt|;
name|regexMap
operator|.
name|put
argument_list|(
name|tuple
operator|.
name|method
argument_list|,
name|replacementMap
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|NameReplacement
argument_list|>
name|replacements
init|=
name|replacementMap
operator|.
name|get
argument_list|(
name|tuple
operator|.
name|argName
argument_list|)
decl_stmt|;
if|if
condition|(
name|replacements
operator|==
literal|null
condition|)
block|{
name|replacements
operator|=
operator|new
name|ArrayList
argument_list|<
name|NameReplacement
argument_list|>
argument_list|()
expr_stmt|;
name|replacementMap
operator|.
name|put
argument_list|(
name|tuple
operator|.
name|argName
argument_list|,
name|replacements
argument_list|)
expr_stmt|;
block|}
name|replacements
operator|.
name|add
argument_list|(
name|nameReplacement
argument_list|)
expr_stmt|;
block|}
comment|// now compile the patterns, all this because Pattern doesn't override equals()!!!
name|this
operator|.
name|methodMap
operator|=
operator|new
name|LinkedHashMap
argument_list|<
name|Pattern
argument_list|,
name|Map
argument_list|<
name|Pattern
argument_list|,
name|List
argument_list|<
name|NameReplacement
argument_list|>
argument_list|>
argument_list|>
argument_list|()
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|NameReplacement
argument_list|>
argument_list|>
argument_list|>
name|method
range|:
name|regexMap
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|Map
argument_list|<
name|Pattern
argument_list|,
name|List
argument_list|<
name|NameReplacement
argument_list|>
argument_list|>
name|argMap
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|Pattern
argument_list|,
name|List
argument_list|<
name|NameReplacement
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|NameReplacement
argument_list|>
argument_list|>
name|arg
range|:
name|method
operator|.
name|getValue
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|argMap
operator|.
name|put
argument_list|(
name|Pattern
operator|.
name|compile
argument_list|(
name|arg
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|,
name|arg
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|methodMap
operator|.
name|put
argument_list|(
name|Pattern
operator|.
name|compile
argument_list|(
name|method
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|,
name|argMap
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|processResults (List<ApiMethodModel> parseResult)
specifier|public
name|List
argument_list|<
name|ApiMethodModel
argument_list|>
name|processResults
parameter_list|(
name|List
argument_list|<
name|ApiMethodModel
argument_list|>
name|parseResult
parameter_list|)
block|{
specifier|final
name|List
argument_list|<
name|ApiMethodModel
argument_list|>
name|result
init|=
operator|new
name|ArrayList
argument_list|<
name|ApiMethodModel
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|ApiMethodModel
name|model
range|:
name|parseResult
control|)
block|{
comment|// look for method name matches
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Pattern
argument_list|,
name|Map
argument_list|<
name|Pattern
argument_list|,
name|List
argument_list|<
name|NameReplacement
argument_list|>
argument_list|>
argument_list|>
name|methodEntry
range|:
name|methodMap
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|methodEntry
operator|.
name|getKey
argument_list|()
operator|.
name|matcher
argument_list|(
name|model
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|matches
argument_list|()
condition|)
block|{
comment|// look for arg name matches
specifier|final
name|List
argument_list|<
name|Argument
argument_list|>
name|updatedArguments
init|=
operator|new
name|ArrayList
argument_list|<
name|Argument
argument_list|>
argument_list|()
decl_stmt|;
specifier|final
name|Map
argument_list|<
name|Pattern
argument_list|,
name|List
argument_list|<
name|NameReplacement
argument_list|>
argument_list|>
name|argMap
init|=
name|methodEntry
operator|.
name|getValue
argument_list|()
decl_stmt|;
for|for
control|(
name|Argument
name|argument
range|:
name|model
operator|.
name|getArguments
argument_list|()
control|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Pattern
argument_list|,
name|List
argument_list|<
name|NameReplacement
argument_list|>
argument_list|>
name|argEntry
range|:
name|argMap
operator|.
name|entrySet
argument_list|()
control|)
block|{
specifier|final
name|Matcher
name|matcher
init|=
name|argEntry
operator|.
name|getKey
argument_list|()
operator|.
name|matcher
argument_list|(
name|argument
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|matcher
operator|.
name|find
argument_list|()
condition|)
block|{
specifier|final
name|List
argument_list|<
name|NameReplacement
argument_list|>
name|adapters
init|=
name|argEntry
operator|.
name|getValue
argument_list|()
decl_stmt|;
for|for
control|(
name|NameReplacement
name|adapter
range|:
name|adapters
control|)
block|{
if|if
condition|(
name|adapter
operator|.
name|type
operator|==
literal|null
operator|||
name|adapter
operator|.
name|type
operator|.
name|isAssignableFrom
argument_list|(
name|argument
operator|.
name|getType
argument_list|()
argument_list|)
condition|)
block|{
name|argument
operator|=
operator|new
name|Argument
argument_list|(
name|matcher
operator|.
name|replaceAll
argument_list|(
name|adapter
operator|.
name|replacement
argument_list|)
argument_list|,
name|argument
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
name|updatedArguments
operator|.
name|add
argument_list|(
name|argument
argument_list|)
expr_stmt|;
block|}
name|model
operator|=
operator|new
name|ApiMethodModel
argument_list|(
name|model
operator|.
name|getUniqueName
argument_list|()
argument_list|,
name|model
operator|.
name|getName
argument_list|()
argument_list|,
name|model
operator|.
name|getResultType
argument_list|()
argument_list|,
name|updatedArguments
argument_list|,
name|model
operator|.
name|getMethod
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|result
operator|.
name|add
argument_list|(
name|model
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
DECL|class|Substitution
specifier|public
specifier|static
class|class
name|Substitution
block|{
DECL|field|method
specifier|private
name|String
name|method
decl_stmt|;
DECL|field|argName
specifier|private
name|String
name|argName
decl_stmt|;
DECL|field|argType
specifier|private
name|String
name|argType
decl_stmt|;
DECL|field|replacement
specifier|private
name|String
name|replacement
decl_stmt|;
comment|/**          * Creates a substitution for all argument types.          * @param method regex to match method name          * @param argName regex to match argument name          * @param replacement replacement text for argument name          */
DECL|method|Substitution (String method, String argName, String replacement)
specifier|public
name|Substitution
parameter_list|(
name|String
name|method
parameter_list|,
name|String
name|argName
parameter_list|,
name|String
name|replacement
parameter_list|)
block|{
name|this
operator|.
name|method
operator|=
name|method
expr_stmt|;
name|this
operator|.
name|argName
operator|=
name|argName
expr_stmt|;
name|this
operator|.
name|replacement
operator|=
name|replacement
expr_stmt|;
block|}
comment|/**          * Creates a substitution for a specific argument type.          * @param method regex to match method name          * @param argName regex to match argument name          * @param argType argument type as String          * @param replacement replacement text for argument name          */
DECL|method|Substitution (String method, String argName, String argType, String replacement)
specifier|public
name|Substitution
parameter_list|(
name|String
name|method
parameter_list|,
name|String
name|argName
parameter_list|,
name|String
name|argType
parameter_list|,
name|String
name|replacement
parameter_list|)
block|{
name|this
operator|.
name|method
operator|=
name|method
expr_stmt|;
name|this
operator|.
name|argName
operator|=
name|argName
expr_stmt|;
name|this
operator|.
name|argType
operator|=
name|argType
expr_stmt|;
name|this
operator|.
name|replacement
operator|=
name|replacement
expr_stmt|;
block|}
DECL|method|validate ()
specifier|public
name|void
name|validate
parameter_list|()
block|{
if|if
condition|(
name|method
operator|==
literal|null
operator|||
name|argName
operator|==
literal|null
operator|||
name|replacement
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Properties method, argName and replacement MUST be provided"
argument_list|)
throw|;
block|}
block|}
block|}
DECL|class|NameReplacement
specifier|private
specifier|static
class|class
name|NameReplacement
block|{
DECL|field|replacement
specifier|private
name|String
name|replacement
decl_stmt|;
DECL|field|type
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|type
decl_stmt|;
block|}
block|}
end_class

end_unit

