class Assets
  def initialize(type)
    @type = type
  end

  def method_missing(method, *_args, &_block)
    get_file_description @type.to_s + '/' + method.to_s
  end

  def self.method_missing(method, *_args, &_block)
    Assets.new method
  end
end
